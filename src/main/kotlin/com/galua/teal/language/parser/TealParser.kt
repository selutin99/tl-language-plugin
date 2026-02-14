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
    override fun parse(
        root: IElementType,
        builder: PsiBuilder,
    ): ASTNode {
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
        return when {
            isKeyword(builder, "local") -> parseLocalStatement(builder)
            isKeyword(builder, "enum") -> parseEnumDeclaration(builder, null)
            isKeyword(builder, "record") -> parseRecordDeclaration(builder, null)
            isKeyword(builder, "interface") -> parseInterfaceDeclaration(builder, null)
            else -> false
        }
    }

    private fun parseLocalStatement(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // local

        return when {
            isKeyword(builder, "enum") -> parseEnumDeclaration(builder, marker)
            isKeyword(builder, "record") -> parseRecordDeclaration(builder, marker)
            isKeyword(builder, "interface") -> parseInterfaceDeclaration(builder, marker)
            else -> {
                parseLocalVariableDeclaration(builder, marker)
                true
            }
        }
    }

    private fun parseLocalVariableDeclaration(
        builder: PsiBuilder,
        marker: PsiBuilder.Marker,
    ) {
        if (builder.tokenType == TealTokenTypes.IDENTIFIER) {
            builder.advanceLexer()
        }

        if (isPunctuation(builder, ":")) {
            parseTypeReference(builder, true)
        }

        if (isOperator(builder, "=")) {
            parseExpression(builder)
        }

        marker.done(TealElementTypes.LOCAL_DECLARATION)
    }

    private fun parseEnumDeclaration(
        builder: PsiBuilder,
        marker: PsiBuilder.Marker?,
    ): Boolean {
        val declarationMarker = marker ?: builder.mark()
        if (!isKeyword(builder, "enum")) {
            declarationMarker.drop()
            return false
        }

        builder.advanceLexer() // enum
        parseTypeIdentifier(builder)
        parseEnumBody(builder)

        declarationMarker.done(TealElementTypes.ENUM_DECLARATION)
        return true
    }

    private fun parseEnumBody(builder: PsiBuilder) {
        while (!builder.eof()) {
            if (isKeyword(builder, "end")) {
                builder.advanceLexer()
                return
            }

            if (builder.tokenType == TealTokenTypes.STRING || builder.tokenType == TealTokenTypes.IDENTIFIER) {
                val memberMarker = builder.mark()
                builder.advanceLexer()
                memberMarker.done(TealElementTypes.ENUM_MEMBER)
                continue
            }

            builder.advanceLexer()
        }
    }

    private fun parseRecordDeclaration(
        builder: PsiBuilder,
        marker: PsiBuilder.Marker?,
    ): Boolean {
        val declarationMarker = marker ?: builder.mark()
        if (!isKeyword(builder, "record")) {
            declarationMarker.drop()
            return false
        }

        builder.advanceLexer() // record
        parseTypeIdentifier(builder)
        parseGenericParameterList(builder)
        parseRecordBody(builder)

        declarationMarker.done(TealElementTypes.RECORD_DECLARATION)
        return true
    }

    private fun parseInterfaceDeclaration(
        builder: PsiBuilder,
        marker: PsiBuilder.Marker?,
    ): Boolean {
        val declarationMarker = marker ?: builder.mark()
        if (!isKeyword(builder, "interface")) {
            declarationMarker.drop()
            return false
        }

        builder.advanceLexer() // interface
        parseTypeIdentifier(builder)
        parseGenericParameterList(builder)
        parseRecordBody(builder)

        declarationMarker.done(TealElementTypes.INTERFACE_DECLARATION)
        return true
    }

    private fun parseRecordBody(builder: PsiBuilder) {
        while (!builder.eof()) {
            when {
                isKeyword(builder, "end") -> {
                    builder.advanceLexer()
                    return
                }

                isKeyword(builder, "is") -> parseIsClause(builder)
                isKeyword(builder, "where") -> parseWhereClause(builder)
                isStartOfRecordField(builder) -> parseRecordField(builder)
                else -> builder.advanceLexer()
            }
        }
    }

    private fun parseRecordField(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // field name

        if (isPunctuation(builder, ":")) {
            parseTypeReference(builder, true)
        }

        marker.done(TealElementTypes.RECORD_FIELD)
    }

    private fun parseIsClause(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // is
        parseTypeReference(builder, false)
        marker.done(TealElementTypes.IS_CLAUSE)
    }

    private fun parseWhereClause(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // where
        while (!builder.eof()) {
            if (isKeyword(builder, "end") || isStartOfRecordField(builder) || isKeyword(builder, "is")) {
                break
            }
            builder.advanceLexer()
        }
        marker.done(TealElementTypes.WHERE_CLAUSE)
    }

    private fun parseTypeReference(
        builder: PsiBuilder,
        consumeLeadingColon: Boolean,
    ): Boolean {
        val marker = builder.mark()
        if (consumeLeadingColon) {
            if (!isPunctuation(builder, ":")) {
                marker.drop()
                return false
            }
            builder.advanceLexer()
        }

        if (!parseTypeExpression(builder)) {
            marker.drop()
            return false
        }

        marker.done(TealElementTypes.TYPE_REFERENCE)
        return true
    }

    private fun parseTypeExpression(builder: PsiBuilder): Boolean {
        return when {
            isKeyword(builder, "function") -> parseFunctionType(builder)
            isBracket(builder, "{") -> parseBraceType(builder)
            isNamedTypeStart(builder) -> parseNamedType(builder)
            else -> false
        }
    }

    private fun parseNamedType(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        when {
            builder.tokenType == TealTokenTypes.IDENTIFIER -> {
                val nameMarker = builder.mark()
                builder.advanceLexer()
                nameMarker.done(TealElementTypes.TYPE_IDENTIFIER)
            }

            builder.tokenType == TealTokenTypes.TYPE || isKeyword(builder, "nil") -> {
                builder.advanceLexer()
            }

            else -> {
                marker.drop()
                return false
            }
        }

        parseGenericArgumentList(builder)
        marker.done(TealElementTypes.NAMED_TYPE)
        return true
    }

    private fun parseGenericParameterList(builder: PsiBuilder) {
        if (!isOperator(builder, "<")) {
            return
        }

        val marker = builder.mark()
        builder.advanceLexer() // <

        parseGenericParameter(builder)
        while (isPunctuation(builder, ",")) {
            builder.advanceLexer()
            parseGenericParameter(builder)
        }

        if (isOperator(builder, ">")) {
            builder.advanceLexer()
        }

        marker.done(TealElementTypes.GENERIC_PARAMETER_LIST)
    }

    private fun parseGenericParameter(builder: PsiBuilder) {
        if (builder.tokenType != TealTokenTypes.IDENTIFIER) {
            return
        }

        val marker = builder.mark()
        builder.advanceLexer()
        marker.done(TealElementTypes.GENERIC_PARAMETER)
    }

    private fun parseGenericArgumentList(builder: PsiBuilder) {
        if (!isOperator(builder, "<")) {
            return
        }

        val marker = builder.mark()
        builder.advanceLexer() // <

        parseTypeExpression(builder)
        while (isPunctuation(builder, ",")) {
            builder.advanceLexer()
            if (!parseTypeExpression(builder)) {
                break
            }
        }

        if (isOperator(builder, ">")) {
            builder.advanceLexer()
        }

        marker.done(TealElementTypes.GENERIC_ARGUMENT_LIST)
    }

    private fun parseFunctionType(builder: PsiBuilder): Boolean {
        if (!isKeyword(builder, "function")) {
            return false
        }

        val marker = builder.mark()
        builder.advanceLexer() // function

        if (isBracket(builder, "(")) {
            builder.advanceLexer()
            val paramsMarker = builder.mark()
            if (!isBracket(builder, ")")) {
                parseTypeList(builder)
            }
            if (isBracket(builder, ")")) {
                builder.advanceLexer()
            }
            paramsMarker.done(TealElementTypes.FUNCTION_TYPE_PARAM_LIST)
        }

        if (isPunctuation(builder, ":")) {
            builder.advanceLexer()
            val returnsMarker = builder.mark()
            parseTypeList(builder)
            returnsMarker.done(TealElementTypes.FUNCTION_TYPE_RETURN_LIST)
        }

        marker.done(TealElementTypes.FUNCTION_TYPE)
        return true
    }

    private fun parseTypeList(builder: PsiBuilder): Boolean {
        if (!parseTypeExpression(builder)) {
            return false
        }

        while (isPunctuation(builder, ",")) {
            builder.advanceLexer()
            if (!parseTypeExpression(builder)) {
                break
            }
        }

        return true
    }

    private fun parseBraceType(builder: PsiBuilder): Boolean {
        if (!isBracket(builder, "{")) {
            return false
        }

        val marker = builder.mark()
        builder.advanceLexer() // {

        if (!parseTypeExpression(builder)) {
            marker.drop()
            return false
        }

        if (isPunctuation(builder, ":")) {
            builder.advanceLexer()
            parseTypeExpression(builder)
            if (isBracket(builder, "}")) {
                builder.advanceLexer()
            }
            marker.done(TealElementTypes.MAP_TYPE)
            return true
        }

        var count = 1
        while (isPunctuation(builder, ",")) {
            builder.advanceLexer()
            if (!parseTypeExpression(builder)) {
                break
            }
            count += 1
        }

        if (isBracket(builder, "}")) {
            builder.advanceLexer()
        }

        marker.done(if (count == 1) TealElementTypes.ARRAY_TYPE else TealElementTypes.TUPLE_TYPE)
        return true
    }

    private fun parseTypeIdentifier(builder: PsiBuilder) {
        if (builder.tokenType != TealTokenTypes.IDENTIFIER) {
            return
        }

        val marker = builder.mark()
        builder.advanceLexer()
        marker.done(TealElementTypes.TYPE_IDENTIFIER)
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

    private fun isKeyword(
        builder: PsiBuilder,
        value: String,
    ): Boolean {
        return builder.tokenType == TealTokenTypes.KEYWORD && builder.tokenText == value
    }

    private fun isOperator(
        builder: PsiBuilder,
        value: String,
    ): Boolean {
        return builder.tokenType == TealTokenTypes.OPERATOR && builder.tokenText == value
    }

    private fun isPunctuation(
        builder: PsiBuilder,
        value: String,
    ): Boolean {
        return builder.tokenType == TealTokenTypes.PUNCTUATION && builder.tokenText == value
    }

    private fun isBracket(
        builder: PsiBuilder,
        value: String,
    ): Boolean {
        return builder.tokenType == TealTokenTypes.BRACKET && builder.tokenText == value
    }

    private fun isNamedTypeStart(builder: PsiBuilder): Boolean {
        return builder.tokenType == TealTokenTypes.TYPE ||
            builder.tokenType == TealTokenTypes.IDENTIFIER ||
            isKeyword(builder, "nil")
    }

    private fun isStartOfRecordField(builder: PsiBuilder): Boolean {
        if (builder.tokenType != TealTokenTypes.IDENTIFIER) {
            return false
        }

        val marker = builder.mark()
        builder.advanceLexer()
        val isField = isPunctuation(builder, ":")
        marker.rollbackTo()
        return isField
    }
}
