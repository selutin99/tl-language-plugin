/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.model

data class TealCheckResult(
    val diagnostics: List<TealCheckDiagnostic>,
    val failureMessage: String?,
) {
    companion object {
        fun success(diagnostics: List<TealCheckDiagnostic>): TealCheckResult =
            TealCheckResult(diagnostics = diagnostics, failureMessage = null)

        fun failure(message: String): TealCheckResult = TealCheckResult(diagnostics = emptyList(), failureMessage = message)
    }
}
