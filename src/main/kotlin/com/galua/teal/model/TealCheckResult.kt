/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.model

/**
 * Result of a background tl check run ready to be applied in the editor
 *
 * @param diagnostics compiler diagnostics that should become editor annotations
 * @param failureMessage user visible message when tl check could not produce diagnostics
 */
data class TealCheckResult(
    val diagnostics: List<TealCheckDiagnostic>,
    val failureMessage: String?,
) {
    companion object {
        /**
         * Creates a successful result from parsed compiler diagnostics
         *
         * @param diagnostics diagnostics reported by tl check
         * @return result without an execution failure message
         */
        fun success(diagnostics: List<TealCheckDiagnostic>): TealCheckResult =
            TealCheckResult(diagnostics = diagnostics, failureMessage = null)

        /**
         * Creates a failed result that can be shown as a weak warning
         *
         * @param message user visible failure message
         * @return result without compiler diagnostics
         */
        fun failure(message: String): TealCheckResult = TealCheckResult(diagnostics = emptyList(), failureMessage = message)
    }
}
