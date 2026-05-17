/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.psi

import com.galua.teal.parser.TealParser
import com.intellij.lang.PsiBuilder
import com.intellij.lang.WhitespacesAndCommentsBinder
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.psi.tree.IElementType

/**
 * Custom Grammar-Kit parser helpers for Teal constructs that need handwritten parsing
 */
object TealParserUtil : GeneratedParserUtilBase() {
    @JvmField
    val MY_LEFT_COMMENT_BINDER: WhitespacesAndCommentsBinder =
        WhitespacesAndCommentsBinder { tokens, atStreamEdge, getter ->
            if (atStreamEdge) return@WhitespacesAndCommentsBinder tokens.size
            var edge = 0
            for (i in tokens.size - 1 downTo 0) {
                val text = getter.get(i).toString()
                if (text.indexOf('\n') >= 0) {
                    // Bind only comments/whitespace directly above; stop at the last line break.
                    edge = i + 1
                    if (text.contains("\n\n")) break
                }
            }
            edge
        }

    @JvmField
    val MY_RIGHT_COMMENT_BINDER: WhitespacesAndCommentsBinder =
        WhitespacesAndCommentsBinder { _, _, _ -> 0 }

    /**
     * Parses any Teal statement alternative in the order expected by the grammar
     *
     * @param builder parser builder positioned at a statement
     * @param level current parser recursion level
     * @return true when one statement alternative was parsed
     */
    @JvmStatic
    fun parseStatement(
        builder: PsiBuilder,
        level: Int,
    ): Boolean {
        if (!recursion_guard_(builder, level, "parseStatement")) {
            return false
        }

        return TealParser.emptyStat(builder, level + 1) ||
            TealParser.doStat(builder, level + 1) ||
            TealParser.whileStat(builder, level + 1) ||
            TealParser.repeatStat(builder, level + 1) ||
            TealParser.ifStat(builder, level + 1) ||
            TealParser.forAStat(builder, level + 1) ||
            TealParser.forBStat(builder, level + 1) ||
            TealParser.classMethodDef(builder, level + 1) ||
            TealParser.funcDef(builder, level + 1) ||
            TealParser.localFuncDef(builder, level + 1) ||
            TealParser.globalFuncDef(builder, level + 1) ||
            TealParser.typeAliasStat(builder, level + 1) ||
            TealParser.recordStat(builder, level + 1) ||
            TealParser.interfaceStat(builder, level + 1) ||
            TealParser.enumStat(builder, level + 1) ||
            TealParser.localDef(builder, level + 1) ||
            TealParser.globalDef(builder, level + 1) ||
            TealParser.returnStat(builder, level + 1) ||
            TealParser.breakStat(builder, level + 1) ||
            TealParser.labelStat(builder, level + 1) ||
            TealParser.gotoStat(builder, level + 1) ||
            TealParser.typedVarStat(builder, level + 1) ||
            TealParser.assignStat(builder, level + 1) ||
            TealParser.exprStat(builder, level + 1)
    }

    /**
     * Parses a Teal block through the generated parser entry point
     *
     * @param builder parser builder positioned at a block
     * @param level current parser recursion level
     * @return true when the block was parsed
     */
    @JvmStatic
    fun lazyBlock(
        builder: PsiBuilder,
        level: Int,
    ): Boolean = TealParser.block(builder, level + 1)

    /**
     * Parses a Teal expression using the handwritten binary precedence parser
     *
     * @param builder parser builder positioned at an expression
     * @param level current parser recursion level
     * @return true when an expression was parsed
     */
    @JvmStatic
    fun parseExpr(
        builder: PsiBuilder,
        level: Int,
    ): Boolean = parseBinaryExpr(builder, level, 0)

    /**
     * Parses binary expressions with precedence climbing and associativity rules
     *
     * @param builder parser builder positioned at the left operand
     * @param level current parser recursion level
     * @param minPrec minimum operator precedence allowed at this position
     * @return true when a binary expression or operand was parsed
     */
    private fun parseBinaryExpr(
        builder: PsiBuilder,
        level: Int,
        minPrec: Int,
    ): Boolean {
        if (!recursion_guard_(builder, level, "parseBinaryExpr")) {
            return false
        }

        var marker = builder.mark()
        if (!parseOperand(builder, level + 1)) {
            marker.drop()
            return false
        }

        while (true) {
            val opType = builder.tokenType ?: break
            val info = BINARY_INFO[opType] ?: break
            if (info.precedence < minPrec) {
                break
            }

            val left = marker.precede()
            builder.advanceLexer()
            val nextMin = if (info.rightAssoc) info.precedence else info.precedence + 1
            if (!parseOperatorRightSide(builder, level + 1, opType, nextMin)) {
                left.drop()
                break
            }

            marker.done(TealTypes.BINARY_EXPR)
            marker = left
        }

        marker.drop()
        return true
    }

    /**
     * Parses the right side of a binary operator with operator specific rules
     *
     * @param builder parser builder positioned after the operator
     * @param level current parser recursion level
     * @param opType operator token type being parsed
     * @param nextMin minimum precedence for nested binary expressions
     * @return true when the operator right side was parsed
     */
    private fun parseOperatorRightSide(
        builder: PsiBuilder,
        level: Int,
        opType: IElementType,
        nextMin: Int,
    ): Boolean =
        when (opType) {
            TealTypes.AS -> parseCastType(builder, level + 1)
            TealTypes.IS -> TealParser.typeexp(builder, level + 1)
            else -> parseBinaryExpr(builder, level + 1, nextMin)
        }

    /**
     * Parses the type expression used by the Teal as cast operator
     *
     * @param builder parser builder positioned at a type expression or parenthesized list
     * @param level current parser recursion level
     * @return true when the cast target type was parsed
     */
    private fun parseCastType(
        builder: PsiBuilder,
        level: Int,
    ): Boolean {
        if (builder.tokenType != TealTypes.LPAREN) {
            return TealParser.typeexp(builder, level + 1)
        }

        val marker = builder.mark()
        builder.advanceLexer()
        val parsed = TealParser.typeexpList(builder, level + 1) && consumeToken(builder, TealTypes.RPAREN)
        if (parsed) {
            marker.drop()
            return true
        }

        marker.rollbackTo()
        return false
    }

    /**
     * Parses a unary expression or primary expression as a binary operand
     *
     * @param builder parser builder positioned at an operand
     * @param level current parser recursion level
     * @return true when an operand was parsed
     */
    private fun parseOperand(
        builder: PsiBuilder,
        level: Int,
    ): Boolean {
        if (TealParser.unaryExpr(builder, level + 1)) {
            return true
        }
        return parsePrimary(builder, level + 1)
    }

    /**
     * Parses a primary expression followed by index or call suffixes
     *
     * @param builder parser builder positioned at a primary expression
     * @param level current parser recursion level
     * @return true when a primary expression was parsed
     */
    private fun parsePrimary(
        builder: PsiBuilder,
        level: Int,
    ): Boolean {
        if (!recursion_guard_(builder, level, "parsePrimary")) {
            return false
        }

        val start = builder.mark()
        val parsedPrefix =
            TealParser.parenExpr(builder, level + 1) ||
                TealParser.nameExpr(builder, level + 1) ||
                TealParser.tableExpr(builder, level + 1) ||
                TealParser.literalExpr(builder, level + 1) ||
                TealParser.closureExpr(builder, level + 1)

        if (!parsedPrefix) {
            start.drop()
            return false
        }

        while (true) {
            val suffix = builder.mark()
            val parsedSuffix =
                TealParser.indexExpr(builder, level + 1) ||
                    TealParser.callExpr(builder, level + 1)
            if (parsedSuffix) {
                suffix.drop()
                continue
            }
            suffix.drop()
            break
        }

        start.drop()
        return true
    }

    /**
     * Binary operator precedence and associativity metadata
     *
     * @param precedence numeric precedence where larger values bind more tightly
     * @param rightAssoc whether operators with this metadata associate to the right
     */
    private data class OpInfo(val precedence: Int, val rightAssoc: Boolean)

    private val BINARY_INFO: Map<IElementType, OpInfo> =
        mapOf(
            TealTypes.OR to OpInfo(1, false),
            TealTypes.AND to OpInfo(2, false),
            TealTypes.IS to OpInfo(3, false),
            TealTypes.LT to OpInfo(4, false),
            TealTypes.LE to OpInfo(4, false),
            TealTypes.GT to OpInfo(4, false),
            TealTypes.GE to OpInfo(4, false),
            TealTypes.EQ to OpInfo(4, false),
            TealTypes.NE to OpInfo(4, false),
            TealTypes.BIT_OR to OpInfo(5, false),
            TealTypes.BIT_TILDE to OpInfo(6, false),
            TealTypes.BIT_AND to OpInfo(7, false),
            TealTypes.BIT_LTLT to OpInfo(8, false),
            TealTypes.BIT_RTRT to OpInfo(8, false),
            TealTypes.CONCAT to OpInfo(9, true),
            TealTypes.PLUS to OpInfo(10, false),
            TealTypes.MINUS to OpInfo(10, false),
            TealTypes.MULT to OpInfo(11, false),
            TealTypes.DIV to OpInfo(11, false),
            TealTypes.DOUBLE_DIV to OpInfo(11, false),
            TealTypes.MOD to OpInfo(11, false),
            TealTypes.EXP to OpInfo(12, true),
            TealTypes.AS to OpInfo(13, false),
        )
}
