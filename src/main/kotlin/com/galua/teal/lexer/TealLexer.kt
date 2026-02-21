/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.lexer

import com.intellij.lexer.LexerBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class TealLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset: Int = 0
    private var endOffset: Int = 0
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var tokenType: IElementType? = null

    override fun start(
        buffer: CharSequence,
        startOffset: Int,
        endOffset: Int,
        initialState: Int,
    ) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        this.tokenType = null
        advance()
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? = tokenType

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = endOffset

    override fun advance() {
        if (tokenEnd >= endOffset) {
            tokenType = null
            return
        }

        tokenStart = tokenEnd
        val current = buffer[tokenStart]

        if (current.isWhitespace()) {
            tokenEnd = consumeWhile(tokenStart) { it.isWhitespace() }
            tokenType = TokenType.WHITE_SPACE
            return
        }

        if (current == '-' && tokenStart + 1 < endOffset && buffer[tokenStart + 1] == '-') {
            tokenEnd = consumeWhile(tokenStart + 2) { it != '\n' && it != '\r' }
            tokenType = TealTokenTypes.COMMENT
            return
        }

        if (current == '\'' || current == '"') {
            val quote = current
            var index = tokenStart + 1
            while (index < endOffset) {
                val ch = buffer[index]
                if (ch == '\\' && index + 1 < endOffset) {
                    index += 2
                    continue
                }
                if (ch == quote) {
                    index++
                    break
                }
                if (ch == '\n' || ch == '\r') {
                    break
                }
                index++
            }
            tokenEnd = index
            tokenType = TealTokenTypes.STRING
            return
        }

        if (current.isDigit()) {
            tokenEnd = consumeWhile(tokenStart) { it.isDigit() || it == '.' || it == '_' }
            tokenType = TealTokenTypes.NUMBER
            return
        }

        if (BRACKETS.contains(current)) {
            tokenEnd = tokenStart + 1
            tokenType = TealTokenTypes.BRACKET
            return
        }

        val operatorLength = operatorLengthAt(tokenStart)
        if (operatorLength > 0) {
            tokenEnd = tokenStart + operatorLength
            tokenType =
                if (PUNCTUATION_SINGLE.contains(current)) {
                    TealTokenTypes.PUNCTUATION
                } else {
                    TealTokenTypes.OPERATOR
                }
            return
        }

        if (current.isIdentifierStart()) {
            tokenEnd = consumeWhile(tokenStart) { it.isIdentifierPart() }
            val text = buffer.subSequence(tokenStart, tokenEnd).toString()
            tokenType =
                when {
                    KEYWORDS.contains(text) -> TealTokenTypes.KEYWORD
                    TYPE_KEYWORDS.contains(text) -> TealTokenTypes.TYPE
                    else -> TealTokenTypes.IDENTIFIER
                }
            return
        }

        tokenEnd = tokenStart + 1
        tokenType = TokenType.BAD_CHARACTER
    }

    private fun consumeWhile(
        startIndex: Int,
        predicate: (Char) -> Boolean,
    ): Int {
        var index = startIndex
        while (index < endOffset && predicate(buffer[index])) {
            index++
        }
        return index
    }

    private fun Char.isIdentifierStart(): Boolean = this == '_' || this.isLetter()

    private fun Char.isIdentifierPart(): Boolean = this == '_' || this.isLetterOrDigit()

    private fun operatorLengthAt(startIndex: Int): Int {
        val current = buffer[startIndex]
        val next = if (startIndex + 1 < endOffset) buffer[startIndex + 1] else null
        val nextNext = if (startIndex + 2 < endOffset) buffer[startIndex + 2] else null

        return when (current) {
            '.' ->
                when {
                    next == '.' && nextNext == '.' -> 3
                    next == '.' -> 2
                    else -> 1
                }

            ':' -> if (next == ':') 2 else 1
            '=' -> if (next == '=') 2 else 1
            '~' -> if (next == '=') 2 else 1
            '<' -> if (next == '=') 2 else 1
            '>' -> if (next == '=') 2 else 1
            '/' -> if (next == '/') 2 else 1
            '-' -> if (next == '>') 2 else 1
            '+' -> 1
            '*' -> 1
            '%' -> 1
            '^' -> 1
            '#' -> 1
            ',' -> 1
            ';' -> 1
            else -> 0
        }
    }

    companion object {
        private val KEYWORDS =
            setOf(
                "and",
                "break",
                "do",
                "else",
                "elseif",
                "end",
                "false",
                "for",
                "function",
                "goto",
                "if",
                "in",
                "local",
                "nil",
                "not",
                "or",
                "repeat",
                "return",
                "then",
                "true",
                "until",
                "while",
                "record",
                "enum",
                "interface",
                "type",
                "as",
                "is",
                "where",
            )

        private val TYPE_KEYWORDS =
            setOf(
                "any",
                "boolean",
                "integer",
                "number",
                "string",
                "table",
                "thread",
                "userdata",
                "function",
            )

        private val BRACKETS = setOf('(', ')', '[', ']', '{', '}')

        private val PUNCTUATION_SINGLE = setOf(',', ';', ':', '.')
    }
}
