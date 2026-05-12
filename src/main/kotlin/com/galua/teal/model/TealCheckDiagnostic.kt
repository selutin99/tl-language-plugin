/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.model

import com.intellij.lang.annotation.HighlightSeverity

data class TealCheckDiagnostic(
    val line: Int,
    val column: Int,
    val message: String,
    val severity: HighlightSeverity,
)
