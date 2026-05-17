/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.parser

import com.galua.teal.model.TealCheckDiagnostic
import com.intellij.lang.annotation.HighlightSeverity
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.isRegularFile
import kotlin.io.path.name

/**
 * Converts stderr produced by tl check into diagnostics for the currently edited file
 */
object TealCheckOutputParser {
    private val warningCountPattern = Regex("""^\d+ warning(s)?:$""")
    private val errorCountPattern = Regex("""^\d+ error(s)?:$""")
    private val diagnosticPattern = Regex("""^(.*?):(\d+):(?:(\d+):)? (.+)$""")

    /**
     * Parses compiler stderr and keeps only diagnostics that belong to the checked file
     *
     * @param stderr raw stderr emitted by tl check
     * @param workDirectory directory used by the compiler process
     * @param originalFilePath path of the opened editor file when available
     * @param temporaryFilePath path of the temporary buffer file passed to tl check
     * @return diagnostics with zero-based editor offsets and IntelliJ severities
     */
    fun parse(
        stderr: String,
        workDirectory: Path,
        originalFilePath: Path?,
        temporaryFilePath: Path,
    ): List<TealCheckDiagnostic> {
        val diagnostics = mutableListOf<TealCheckDiagnostic>()
        var warningSection = false

        for (line in stderr.splitToSequence(Regex("""\r?\n"""))) {
            val compilerLine = line.trimEnd('\r')
            when {
                warningCountPattern.matches(compilerLine) -> warningSection = true
                errorCountPattern.matches(compilerLine) -> warningSection = false
                else ->
                    parseDiagnosticLine(
                        compilerLine,
                        workDirectory,
                        originalFilePath,
                        temporaryFilePath,
                        warningSection,
                    )?.let(diagnostics::add)
            }
        }

        return diagnostics
    }

    /**
     * Parses one diagnostic line and maps temporary file paths back to the editor file
     *
     * @param compilerLine single stderr line emitted by tl check
     * @param workDirectory directory used to resolve relative compiler paths
     * @param originalFilePath path of the opened editor file when available
     * @param temporaryFilePath path of the temporary buffer file passed to tl check
     * @param warningSection whether the parser is currently inside the warning section
     * @return diagnostic for the current file or null when the line is unrelated
     */
    private fun parseDiagnosticLine(
        compilerLine: String,
        workDirectory: Path,
        originalFilePath: Path?,
        temporaryFilePath: Path,
        warningSection: Boolean,
    ): TealCheckDiagnostic? {
        val match = diagnosticPattern.matchEntire(compilerLine) ?: return null
        val filePath = resolveCompilerPath(workDirectory, match.groupValues[1])
        if (!isCurrentFileDiagnostic(filePath, originalFilePath, temporaryFilePath)) {
            return null
        }

        val line = match.groupValues[2].toIntOrNull()?.minus(1) ?: return null
        val column = match.groupValues[3].takeIf(String::isNotBlank)?.toIntOrNull()?.minus(1) ?: 0
        val message =
            match.groupValues[4]
                .replace(temporaryFilePath.absolutePathString(), originalFilePath?.absolutePathString().orEmpty())
                .replace(temporaryFilePath.name, originalFilePath?.name.orEmpty())

        return TealCheckDiagnostic(
            line = line.coerceAtLeast(0),
            column = column.coerceAtLeast(0),
            message = message,
            severity = if (warningSection) HighlightSeverity.WARNING else HighlightSeverity.ERROR,
        )
    }

    /**
     * Resolves a compiler file name against the process working directory
     *
     * @param workDirectory directory used by the compiler process
     * @param fileName file name or path printed by tl check
     * @return normalized absolute or work directory relative path
     */
    private fun resolveCompilerPath(
        workDirectory: Path,
        fileName: String,
    ): Path {
        val path = Path.of(fileName)
        return if (path.isAbsolute) {
            path.normalize()
        } else {
            workDirectory.resolve(path).normalize()
        }
    }

    /**
     * Checks whether a compiler diagnostic points at the edited file or its temporary copy
     *
     * @param filePath path printed by tl check
     * @param originalFilePath path of the opened editor file when available
     * @param temporaryFilePath path of the temporary buffer file passed to tl check
     * @return true when the diagnostic belongs to the current editor buffer
     */
    private fun isCurrentFileDiagnostic(
        filePath: Path,
        originalFilePath: Path?,
        temporaryFilePath: Path,
    ): Boolean {
        if (sameFileOrPath(filePath, temporaryFilePath)) {
            return true
        }

        return originalFilePath != null && sameFileOrPath(filePath, originalFilePath)
    }

    /**
     * Compares paths by normalized text first and by filesystem identity when possible
     *
     * @param left first path to compare
     * @param right second path to compare
     * @return true when both paths refer to the same file
     */
    private fun sameFileOrPath(
        left: Path,
        right: Path,
    ): Boolean {
        val normalizedLeft = left.normalize()
        val normalizedRight = right.normalize()
        if (normalizedLeft == normalizedRight) {
            return true
        }

        return normalizedLeft.isRegularFile() &&
            normalizedRight.isRegularFile() &&
            runCatching { java.nio.file.Files.isSameFile(normalizedLeft, normalizedRight) }
                .getOrDefault(false)
    }
}
