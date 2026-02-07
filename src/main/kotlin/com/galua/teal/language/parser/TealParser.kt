/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.language.parser

import com.galua.teal.language.lexer.TealTokenTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

class TealParser : PsiParser {

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()
        parseFile(builder)
        rootMarker.done(root)
        return builder.treeBuilt
    }

    private fun parseFile(builder: PsiBuilder) {
        while (!builder.eof()) {
            if (!parseStatement(builder)) {
                builder.advanceLexer()
            }
        }
    }

    private fun parseStatement(builder: PsiBuilder): Boolean {
        if (isKeyword(builder, "local")) {
            parseLocalDeclaration(builder)
            return true
        }
        return false
    }

    private fun parseLocalDeclaration(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // local

        if (builder.tokenType == TealTokenTypes.IDENTIFIER) {
            builder.advanceLexer()
        }

        if (isPunctuation(builder, ":")) {
            parseTypeReference(builder)
        }

        if (isOperator(builder, "=")) {
            parseExpression(builder)
        }

        marker.done(TealElementTypes.LOCAL_DECLARATION)
    }

    private fun parseTypeReference(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // :
        if (builder.tokenType == TealTokenTypes.TYPE || builder.tokenType == TealTokenTypes.IDENTIFIER) {
            builder.advanceLexer()
        }
        marker.done(TealElementTypes.TYPE_REFERENCE)
    }

    private fun parseExpression(builder: PsiBuilder) {
        builder.advanceLexer() // =
        val marker = builder.mark()
        when {
            builder.tokenType == TealTokenTypes.NUMBER -> {
                builder.advanceLexer()
                marker.done(TealElementTypes.NUMBER_LITERAL)
            }
            builder.tokenType == TealTokenTypes.STRING -> {
                builder.advanceLexer()
                marker.done(TealElementTypes.STRING_LITERAL)
            }
            isKeyword(builder, "true") || isKeyword(builder, "false") -> {
                builder.advanceLexer()
                marker.done(TealElementTypes.BOOLEAN_LITERAL)
            }
            builder.tokenType == TealTokenTypes.IDENTIFIER -> {
                builder.advanceLexer()
                marker.done(TealElementTypes.IDENTIFIER)
            }
            else -> marker.drop()
        }
    }

    private fun isKeyword(builder: PsiBuilder, value: String): Boolean {
        return builder.tokenType == TealTokenTypes.KEYWORD && builder.tokenText == value
    }

    private fun isOperator(builder: PsiBuilder, value: String): Boolean {
        return builder.tokenType == TealTokenTypes.OPERATOR && builder.tokenText == value
    }

    private fun isPunctuation(builder: PsiBuilder, value: String): Boolean {
        return builder.tokenType == TealTokenTypes.PUNCTUATION && builder.tokenText == value
    }
}
