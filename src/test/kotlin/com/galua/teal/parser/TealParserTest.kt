/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.parser

import com.galua.teal.TestUtils
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class TealParserTest : BasePlatformTestCase() {
    fun testParsesVisibleTypeDeclarations() {
        // given
        myFixture.configureByText(
            "types.tl",
            TestUtils.loadTestResource(VISIBLE_TYPE_DECLARATIONS_RESOURCE),
        )

        // when
        val errors = PsiTreeUtil.findChildrenOfType(myFixture.file, PsiErrorElement::class.java)

        // then
        assertEmpty(errors.map { it.errorDescription })
    }

    fun testParsesRequireCallAsExpression() {
        // given
        myFixture.configureByText(
            "require-call.tl",
            "local test = require(\"module\")",
        )

        // when
        val errors = PsiTreeUtil.findChildrenOfType(myFixture.file, PsiErrorElement::class.java)

        // then
        assertEmpty(errors.map { it.errorDescription })
    }

    fun testParsesRequireTypeDeclaration() {
        // given
        myFixture.configureByText(
            "require-type.tl",
            "local type Module = require(\"module\")",
        )

        // when
        val errors = PsiTreeUtil.findChildrenOfType(myFixture.file, PsiErrorElement::class.java)

        // then
        assertEmpty(errors.map { it.errorDescription })
    }

    private companion object {
        private const val VISIBLE_TYPE_DECLARATIONS_RESOURCE = "files/visible-type-declarations.tl"
    }
}
