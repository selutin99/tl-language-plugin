/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.annotator

import com.galua.teal.model.TealCheckDiagnostic
import com.galua.teal.model.TealCheckInput
import com.galua.teal.model.TealCheckResult
import com.galua.teal.parser.TealCheckOutputParser
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempFile
import kotlin.io.path.exists
import kotlin.io.path.writeText

/**
 * Runs tl check for the current editor buffer and converts compiler diagnostics into annotations
 */
class TealCheckAnnotator : ExternalAnnotator<TealCheckInput, TealCheckResult>() {
    /**
     * Captures the current file text and resolves the working directory for tl check
     *
     * @param file psi file that should be checked by the Teal compiler
     * @return input snapshot for the background annotator or null for blank files
     */
    override fun collectInformation(file: PsiFile): TealCheckInput? {
        val text = file.text
        if (text.isBlank()) {
            return null
        }

        val originalFilePath = file.virtualFile?.path?.let { Path.of(it) }
        val workDirectory = findWorkDirectory(file, originalFilePath)

        return TealCheckInput(
            text = text,
            originalFilePath = originalFilePath,
            workDirectory = workDirectory,
        )
    }

    /**
     * Runs tl check on a temporary copy of the editor buffer
     *
     * @param collectedInfo input snapshot collected on the EDT
     * @return parsed compiler result or null when there is no input to check
     */
    override fun doAnnotate(collectedInfo: TealCheckInput?): TealCheckResult? {
        val input = collectedInfo ?: return null
        ProgressManager.checkCanceled()

        val temporaryFile = createTempFile(prefix = TEMP_FILE_PREFIX, suffix = ".tl")
        return try {
            temporaryFile.writeText(input.text, StandardCharsets.UTF_8)
            val commandLine =
                GeneralCommandLine(
                    COMPILER_EXECUTABLE,
                    "check",
                    "--quiet",
                    temporaryFile.absolutePathString(),
                ).withWorkDirectory(input.workDirectory.absolutePathString())

            val output =
                CapturingProcessHandler(commandLine)
                    .runProcess(COMPILER_TIMEOUT_MS)

            ProgressManager.checkCanceled()
            if (output.isTimeout) {
                TealCheckResult.failure("tl check timed out")
            } else {
                TealCheckResult.success(
                    TealCheckOutputParser.parse(
                        stderr = output.stderr,
                        workDirectory = input.workDirectory,
                        originalFilePath = input.originalFilePath,
                        temporaryFilePath = temporaryFile,
                    ),
                )
            }
        } catch (exception: Exception) {
            LOG.warn("Cannot run tl check", exception)
            TealCheckResult.failure("Cannot run tl check: ${exception.message ?: exception.javaClass.simpleName}")
        } finally {
            runCatching { Files.deleteIfExists(temporaryFile) }
        }
    }

    /**
     * Applies tl check diagnostics or execution failures to the editor annotation holder
     *
     * @param file psi file currently receiving annotations
     * @param annotationResult result produced by the background annotator
     * @param holder annotation holder for the current highlighting pass
     */
    override fun apply(
        file: PsiFile,
        annotationResult: TealCheckResult?,
        holder: AnnotationHolder,
    ) {
        val result = annotationResult ?: return
        val document = PsiDocumentManager.getInstance(file.project).getDocument(file) ?: return

        result.failureMessage?.let { message ->
            holder.newAnnotation(HighlightSeverity.WEAK_WARNING, message)
                .range(firstVisibleRange(document))
                .create()
            return
        }

        for (diagnostic in result.diagnostics) {
            val range = diagnosticRange(document, diagnostic) ?: continue
            holder.newAnnotation(diagnostic.severity, diagnostic.message)
                .range(range)
                .tooltip("tl check: ${diagnostic.message}")
                .create()
        }
    }

    /**
     * Chooses the directory where tl check should run for the current file
     *
     * @param file psi file used to fall back to the project root
     * @param originalFilePath path of the opened editor file when available
     * @return closest Teal config root, project root, source directory, or current directory
     */
    private fun findWorkDirectory(
        file: PsiFile,
        originalFilePath: Path?,
    ): Path {
        val sourceDirectory = originalFilePath?.parent
        val tealRoot = sourceDirectory?.let(::findTealConfigRoot)
        val projectRoot = file.project.basePath?.let { Path.of(it) }
        return tealRoot ?: projectRoot ?: sourceDirectory ?: Path.of(".").toAbsolutePath().normalize()
    }

    /**
     * Searches upward for the nearest tlconfig.lua file
     *
     * @param startDirectory directory where the search begins
     * @return directory containing tlconfig.lua or null when no config is found
     */
    private fun findTealConfigRoot(startDirectory: Path): Path? {
        var directory: Path? = startDirectory
        while (directory != null) {
            if (directory.resolve(TEAL_CONFIG_FILE).exists()) {
                return directory
            }
            directory = directory.parent
        }
        return null
    }

    /**
     * Converts a compiler line and column diagnostic into an editor text range
     *
     * @param document editor document that owns the highlighted text
     * @param diagnostic diagnostic with zero-based line and column offsets
     * @return text range to highlight or null when the diagnostic points outside the document
     */
    private fun diagnosticRange(
        document: Document,
        diagnostic: TealCheckDiagnostic,
    ): TextRange? {
        if (diagnostic.line >= document.lineCount) {
            return null
        }

        val lineStart = document.getLineStartOffset(diagnostic.line)
        val lineEnd = document.getLineEndOffset(diagnostic.line)
        val start = (lineStart + diagnostic.column).coerceIn(lineStart, lineEnd)
        val end = findTokenEnd(document, start, lineEnd)
        return TextRange(start, end)
    }

    /**
     * Extends a diagnostic start offset to the end of the current identifier token
     *
     * @param document editor document that owns the highlighted text
     * @param start start offset reported by the compiler
     * @param lineEnd end offset of the diagnostic line
     * @return token end offset or a one character fallback range end
     */
    private fun findTokenEnd(
        document: Document,
        start: Int,
        lineEnd: Int,
    ): Int {
        val chars = document.charsSequence
        var end = start
        while (end < lineEnd && isDiagnosticTokenChar(chars[end])) {
            end++
        }

        if (end > start) {
            return end
        }

        return (start + 1).coerceAtMost(document.textLength)
    }

    private fun isDiagnosticTokenChar(char: Char): Boolean = char.isLetterOrDigit() || char == '_'

    /**
     * Returns a minimal visible range for file level execution warnings
     *
     * @param document editor document that should receive the warning
     * @return empty range for empty files or the first visible character range
     */
    private fun firstVisibleRange(document: Document): TextRange {
        if (document.textLength == 0) {
            return TextRange.EMPTY_RANGE
        }

        return TextRange(0, 1)
    }

    private companion object {
        private const val COMPILER_EXECUTABLE = "tl"
        private const val COMPILER_TIMEOUT_MS = 10_000
        private const val TEAL_CONFIG_FILE = "tlconfig.lua"
        private const val TEMP_FILE_PREFIX = "tl-language-plugin-buffer-"

        private val LOG = Logger.getInstance(TealCheckAnnotator::class.java)
    }
}
