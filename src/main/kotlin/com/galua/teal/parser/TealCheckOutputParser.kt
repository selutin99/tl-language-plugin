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

object TealCheckOutputParser {
    private val warningCountPattern = Regex("""^\d+ warning(s)?:$""")
    private val errorCountPattern = Regex("""^\d+ error(s)?:$""")
    private val diagnosticPattern = Regex("""^(.*?):(\d+):(?:(\d+):)? (.+)$""")

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
