/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.highlighting

import com.galua.teal.lexer.TealLexer
import com.galua.teal.lexer.TealTokenSets
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class TealSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = TealLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        when {
            TealTokenSets.DOC_COMMENTS.contains(tokenType) -> DOC_COMMENT_KEYS
            TealTokenSets.COMMENTS.contains(tokenType) -> COMMENT_KEYS
            TealTokenSets.SHEBANGS.contains(tokenType) -> COMMENT_KEYS
            TealTokenSets.STRING_LITERALS.contains(tokenType) -> STRING_KEYS
            TealTokenSets.NUMBERS.contains(tokenType) -> NUMBER_KEYS
            TealTokenSets.KEYWORDS.contains(tokenType) -> KEYWORD_KEYS
            TealTokenSets.OPERATORS.contains(tokenType) -> OPERATOR_KEYS
            TealTokenSets.BRACKETS.contains(tokenType) -> BRACKET_KEYS
            TealTokenSets.PUNCTUATION.contains(tokenType) -> PUNCTUATION_KEYS
            TealTokenSets.IDENTIFIERS.contains(tokenType) -> IDENTIFIER_KEYS
            TealTokenSets.BAD_CHARACTERS.contains(tokenType) -> BAD_CHAR_KEYS
            else -> EMPTY
        }

    companion object {
        @JvmField
        val TYPE_IDENTIFIER_KEY: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey(
                "TEAL_TYPE_IDENTIFIER",
                DefaultLanguageHighlighterColors.CLASS_NAME,
            )

        @JvmField
        val NAME_DEFINITION_KEY: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey(
                "TEAL_NAME_DEFINITION",
                DefaultLanguageHighlighterColors.LOCAL_VARIABLE,
            )

        @JvmField
        val GENERIC_PARAMETER_KEY: TextAttributesKey =
            TextAttributesKey.createTextAttributesKey(
                "TEAL_GENERIC_PARAMETER",
                DefaultLanguageHighlighterColors.CLASS_NAME,
            )

        private val KEYWORD_KEYS =
            arrayOf(
                TextAttributesKey.createTextAttributesKey(
                    "TEAL_KEYWORD",
                    DefaultLanguageHighlighterColors.KEYWORD,
                ),
            )
        private val STRING_KEYS =
            arrayOf(
                TextAttributesKey.createTextAttributesKey(
                    "TEAL_STRING",
                    DefaultLanguageHighlighterColors.STRING,
                ),
            )
        private val NUMBER_KEYS =
            arrayOf(
                TextAttributesKey.createTextAttributesKey(
                    "TEAL_NUMBER",
                    DefaultLanguageHighlighterColors.NUMBER,
                ),
            )
        private val COMMENT_KEYS =
            arrayOf(
                TextAttributesKey.createTextAttributesKey(
                    "TEAL_COMMENT",
                    DefaultLanguageHighlighterColors.LINE_COMMENT,
                ),
            )
        private val DOC_COMMENT_KEYS =
            arrayOf(
                TextAttributesKey.createTextAttributesKey(
                    "TEAL_DOC_COMMENT",
                    DefaultLanguageHighlighterColors.DOC_COMMENT,
                ),
            )
        private val IDENTIFIER_KEYS =
            arrayOf(
                TextAttributesKey.createTextAttributesKey(
                    "TEAL_IDENTIFIER",
                    DefaultLanguageHighlighterColors.IDENTIFIER,
                ),
            )
        private val OPERATOR_KEYS =
            arrayOf(
                TextAttributesKey.createTextAttributesKey(
                    "TEAL_OPERATOR",
                    DefaultLanguageHighlighterColors.OPERATION_SIGN,
                ),
            )
        private val BRACKET_KEYS =
            arrayOf(
                TextAttributesKey.createTextAttributesKey(
                    "TEAL_BRACKET",
                    DefaultLanguageHighlighterColors.BRACKETS,
                ),
            )
        private val PUNCTUATION_KEYS =
            arrayOf(
                TextAttributesKey.createTextAttributesKey(
                    "TEAL_PUNCTUATION",
                    DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL,
                ),
            )
        private val BAD_CHAR_KEYS =
            arrayOf(
                TextAttributesKey.createTextAttributesKey(
                    "TEAL_BAD_CHARACTER",
                    HighlighterColors.BAD_CHARACTER,
                ),
            )
        private val EMPTY = emptyArray<TextAttributesKey>()
    }
}
