/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.annotator

import com.galua.teal.TestUtils
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.assertAll

class TealAnnotatorTest : BasePlatformTestCase() {
    fun testDoesNotHighlightFunctionCallWithStringArgument() {
        // given
        myFixture.configureByText(
            "test.tl",
            TestUtils.loadTestResource("files/function-call-with-string-argument.tl")
        )

        // when
        val errors =
            myFixture.doHighlighting()
                .filter { it.severity == HighlightSeverity.ERROR }

        // then
        assertEmpty(errors)
    }

    fun testHighlightsStringPlusStringOperatorError() {
        // given
        myFixture.configureByText(
            "test.tl",
            TestUtils.loadTestResource("files/string-plus-string-operator-error.tl")
        )

        // when
        val highlights = myFixture.doHighlighting()

        // then
        val errors =
            highlights
                .filter { it.severity == HighlightSeverity.ERROR }
                .map { it.description }

        val operatorError =
            highlights.find {
                it.description == "cannot use operator '+' for types string \"1\" and string \"2\""
            }
        val operatorText =
            operatorError?.let { myFixture.file.text.substring(it.actualStartOffset, it.actualEndOffset) }

        assertAll(
            { assertContainsElements(errors, "cannot use operator '+' for types string \"1\" and string \"2\"") },
            { assertNotNull(operatorError) },
            { assertEquals("+", operatorText) },
        )
    }

    fun testHighlightsLocalDeclarationLiteralTypeMismatch() {
        // given
        myFixture.configureByText(
            "test.tl",
            TestUtils.loadTestResource("files/local-declaration-literal-type-mismatch.tl")
        )

        // when
        val highlights = myFixture.doHighlighting()

        // then
        val errors =
            highlights
                .filter { it.severity == HighlightSeverity.ERROR }
                .map { it.description }

        val mismatch =
            highlights.find { it.description == "Cannot assign number to string" }
        val mismatchText =
            mismatch?.let { myFixture.file.text.substring(it.actualStartOffset, it.actualEndOffset) }

        assertAll(
            { assertContainsElements(errors, "Unknown type: erijsdfjsdfj", "Cannot assign number to string") },
            { assertNotNull(mismatch) },
            { assertEquals("232", mismatchText) },
        )
    }
}
