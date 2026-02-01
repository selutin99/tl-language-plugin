package com.galua.teal.language

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
        TealTokenTypes.STRING -> STRING_KEYS
        TealTokenTypes.NUMBER -> NUMBER_KEYS
        TealTokenTypes.COMMENT -> COMMENT_KEYS
        TealTokenTypes.IDENTIFIER -> IDENTIFIER_KEYS
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
        private val BAD_CHAR_KEYS = arrayOf(TextAttributesKey.createTextAttributesKey(
            "TEAL_BAD_CHARACTER",
            HighlighterColors.BAD_CHARACTER
        ))
        private val EMPTY = emptyArray<TextAttributesKey>()
    }
}
