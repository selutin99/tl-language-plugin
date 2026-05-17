/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.model

import com.intellij.lang.annotation.HighlightSeverity

/**
 * Diagnostic reported by the Teal compiler for a source location
 *
 * @param line zero-based line offset in the editor document
 * @param column zero-based column offset inside the line
 * @param message human-readable diagnostic text reported by tl check
 * @param severity highlighting severity mapped from the compiler section to IntelliJ
 */
data class TealCheckDiagnostic(
    val line: Int,
    val column: Int,
    val message: String,
    val severity: HighlightSeverity,
)
