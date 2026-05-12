/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.highlighting

import com.galua.teal.psi.TealTypes
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class TealSyntaxHighlighterTest : BasePlatformTestCase() {
    fun testHighlightsTealDeclarationKeywords() {
        // given - when
        val highlighter = TealSyntaxHighlighter()

        // then
        assertTrue(highlighter.getTokenHighlights(TealTypes.REQUIRE).isNotEmpty())
        assertTrue(highlighter.getTokenHighlights(TealTypes.USERDATA).isNotEmpty())
        assertTrue(highlighter.getTokenHighlights(TealTypes.WHERE).isNotEmpty())
        assertTrue(highlighter.getTokenHighlights(TealTypes.METAMETHOD).isNotEmpty())
    }
}
