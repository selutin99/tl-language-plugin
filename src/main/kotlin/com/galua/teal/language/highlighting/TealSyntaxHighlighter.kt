/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.language.highlighting

import com.galua.teal.language.lexer.TealLexer
import com.galua.teal.language.lexer.TealTokenTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class TealSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer = TealLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> = when (tokenType) {
        TealTokenTypes.KEYWORD -> KEYWORD_KEYS
        TealTokenTypes.TYPE -> TYPE_KEYS
        TealTokenTypes.STRING -> STRING_KEYS
        TealTokenTypes.NUMBER -> NUMBER_KEYS
        TealTokenTypes.COMMENT -> COMMENT_KEYS
        TealTokenTypes.IDENTIFIER -> IDENTIFIER_KEYS
        TealTokenTypes.OPERATOR -> OPERATOR_KEYS
        TealTokenTypes.BRACKET -> BRACKET_KEYS
        TealTokenTypes.PUNCTUATION -> PUNCTUATION_KEYS
        TokenType.BAD_CHARACTER -> BAD_CHAR_KEYS
        else -> EMPTY
    }

    companion object {
        private val KEYWORD_KEYS = arrayOf(TextAttributesKey.createTextAttributesKey(
            "TEAL_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        ))
        private val STRING_KEYS = arrayOf(TextAttributesKey.createTextAttributesKey(
            "TEAL_STRING",
            DefaultLanguageHighlighterColors.STRING
        ))
        private val TYPE_KEYS = arrayOf<TextAttributesKey>(TextAttributesKey.createTextAttributesKey(
            "TEAL_TYPE",
            DefaultLanguageHighlighterColors.KEYWORD
        ))
        private val NUMBER_KEYS = arrayOf(TextAttributesKey.createTextAttributesKey(
            "TEAL_NUMBER",
            DefaultLanguageHighlighterColors.NUMBER
        ))
        private val COMMENT_KEYS = arrayOf(TextAttributesKey.createTextAttributesKey(
            "TEAL_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT
        ))
        private val IDENTIFIER_KEYS = arrayOf(TextAttributesKey.createTextAttributesKey(
            "TEAL_IDENTIFIER",
            DefaultLanguageHighlighterColors.IDENTIFIER
        ))
        private val OPERATOR_KEYS = arrayOf(TextAttributesKey.createTextAttributesKey(
            "TEAL_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        ))
        private val BRACKET_KEYS = arrayOf(TextAttributesKey.createTextAttributesKey(
            "TEAL_BRACKET",
            DefaultLanguageHighlighterColors.BRACKETS
        ))
        private val PUNCTUATION_KEYS = arrayOf(TextAttributesKey.createTextAttributesKey(
            "TEAL_PUNCTUATION",
            DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL
        ))
        private val BAD_CHAR_KEYS = arrayOf(TextAttributesKey.createTextAttributesKey(
            "TEAL_BAD_CHARACTER",
            HighlighterColors.BAD_CHARACTER
        ))
        private val EMPTY = emptyArray<TextAttributesKey>()
    }
}
