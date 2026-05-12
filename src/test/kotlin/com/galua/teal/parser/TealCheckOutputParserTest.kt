/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.parser

import com.galua.teal.TestUtils
import com.galua.teal.model.TealCheckDiagnostic
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class TealCheckOutputParserTest : BasePlatformTestCase() {
    fun testParsesErrorsForTemporaryBuffer() {
        // given
        val originalFile = TestUtils.testResourcePath(LOCAL_DECLARATION_RESOURCE)
        val temporaryFile = TestUtils.testResourcePath(STRING_PLUS_RESOURCE)
        val stderr =
            """
            1 error:
            ${temporaryFile.toAbsolutePath()}:3:10: unknown type erijsdfjsdfj
            """.trimIndent()

        // when
        val diagnostics =
            TealCheckOutputParser.parse(
                stderr = stderr,
                workDirectory = TestUtils.testResourcesRoot,
                originalFilePath = originalFile,
                temporaryFilePath = temporaryFile,
            )

        // then
        assertSize(1, diagnostics)
        assertEquals(
            TealCheckDiagnostic(
                line = 2,
                column = 9,
                message = "unknown type erijsdfjsdfj",
                severity = HighlightSeverity.ERROR,
            ),
            diagnostics.single(),
        )
    }

    fun testParsesWarningSection() {
        // given
        val originalFile = TestUtils.testResourcePath(LOCAL_DECLARATION_RESOURCE)
        val temporaryFile = TestUtils.testResourcePath(STRING_PLUS_RESOURCE)
        val stderr =
            """
            1 warning:
            $LOCAL_DECLARATION_RESOURCE:2: unused variable x
            """.trimIndent()

        // when
        val diagnostics =
            TealCheckOutputParser.parse(
                stderr = stderr,
                workDirectory = TestUtils.testResourcesRoot,
                originalFilePath = originalFile,
                temporaryFilePath = temporaryFile,
            )

        // then
        assertSize(1, diagnostics)
        assertEquals(HighlightSeverity.WARNING, diagnostics.single().severity)
        assertEquals(1, diagnostics.single().line)
        assertEquals(0, diagnostics.single().column)
        assertEquals("unused variable x", diagnostics.single().message)
    }

    private companion object {
        private const val LOCAL_DECLARATION_RESOURCE = "files/local-declaration-literal-type-mismatch.tl"
        private const val STRING_PLUS_RESOURCE = "files/string-plus-string-operator-error.tl"
    }
}
