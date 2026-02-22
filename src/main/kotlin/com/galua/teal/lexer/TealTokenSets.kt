/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.lexer

import com.galua.teal.psi.TealTypes
import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet

object TealTokenSets {
    val COMMENTS: TokenSet =
        TokenSet.create(
            TealTypes.SHORT_COMMENT,
            TealTypes.DOC_COMMENT,
            TealTypes.BLOCK_COMMENT,
        )

    val DOC_COMMENTS: TokenSet =
        TokenSet.create(
            TealTypes.DOC_COMMENT,
        )

    val SHEBANGS: TokenSet =
        TokenSet.create(
            TealTypes.SHEBANG,
            TealTypes.SHEBANG_CONTENT,
        )

    val STRING_LITERALS: TokenSet =
        TokenSet.create(
            TealTypes.STRING,
        )

    val NUMBERS: TokenSet =
        TokenSet.create(
            TealTypes.NUMBER,
        )

    val IDENTIFIERS: TokenSet =
        TokenSet.create(
            TealTypes.ID,
        )

    val KEYWORDS: TokenSet =
        TokenSet.create(
            TealTypes.AND,
            TealTypes.AS,
            TealTypes.BREAK,
            TealTypes.DO,
            TealTypes.ELSE,
            TealTypes.ELSEIF,
            TealTypes.END,
            TealTypes.ENUM,
            TealTypes.FALSE,
            TealTypes.FOR,
            TealTypes.FUNCTION,
            TealTypes.GLOBAL,
            TealTypes.GOTO,
            TealTypes.IF,
            TealTypes.IN,
            TealTypes.INTERFACE,
            TealTypes.IS,
            TealTypes.LOCAL,
            TealTypes.NIL,
            TealTypes.NOT,
            TealTypes.OR,
            TealTypes.RECORD,
            TealTypes.REPEAT,
            TealTypes.RETURN,
            TealTypes.THEN,
            TealTypes.TRUE,
            TealTypes.TYPE,
            TealTypes.UNTIL,
            TealTypes.WHILE,
            TealTypes.REGION,
            TealTypes.ENDREGION,
        )

    val OPERATORS: TokenSet =
        TokenSet.create(
            TealTypes.ASSIGN,
            TealTypes.EQ,
            TealTypes.NE,
            TealTypes.LE,
            TealTypes.GE,
            TealTypes.LT,
            TealTypes.GT,
            TealTypes.PLUS,
            TealTypes.MINUS,
            TealTypes.MULT,
            TealTypes.DIV,
            TealTypes.DOUBLE_DIV,
            TealTypes.MOD,
            TealTypes.EXP,
            TealTypes.CONCAT,
            TealTypes.BIT_OR,
            TealTypes.BIT_AND,
            TealTypes.BIT_LTLT,
            TealTypes.BIT_RTRT,
            TealTypes.BIT_TILDE,
            TealTypes.GETN,
        )

    val BRACKETS: TokenSet =
        TokenSet.create(
            TealTypes.LPAREN,
            TealTypes.RPAREN,
            TealTypes.LBRACK,
            TealTypes.RBRACK,
            TealTypes.LCURLY,
            TealTypes.RCURLY,
        )

    val PUNCTUATION: TokenSet =
        TokenSet.create(
            TealTypes.COMMA,
            TealTypes.SEMI,
            TealTypes.COLON,
            TealTypes.DOT,
            TealTypes.DOUBLE_COLON,
            TealTypes.ELLIPSIS,
            TealTypes.QUESTION,
        )

    val BAD_CHARACTERS: TokenSet =
        TokenSet.create(
            TokenType.BAD_CHARACTER,
        )
}
