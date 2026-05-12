/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.annotator

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class TealAnnotatorTest : BasePlatformTestCase() {
    fun testHighlightsTypeAndNameIdentifiers() {
        // given
        myFixture.configureByText(
            "test.tl",
            "local value: string = \"test\"",
        )

        // when
        val highlightedTexts =
            myFixture.doHighlighting()
                .filter { it.severity == HighlightSeverity.INFORMATION }
                .map { myFixture.file.text.substring(it.actualStartOffset, it.actualEndOffset) }

        // then
        assertContainsElements(highlightedTexts, "value", "string")
    }
}
