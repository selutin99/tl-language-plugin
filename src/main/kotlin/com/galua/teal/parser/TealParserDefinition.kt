/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.parser

import com.galua.teal.lexer.TealLexer
import com.galua.teal.lexer.TealTokenTypes
import com.galua.teal.psi.TealArrayType
import com.galua.teal.psi.TealBooleanLiteral
import com.galua.teal.psi.TealEnumDeclaration
import com.galua.teal.psi.TealFile
import com.galua.teal.psi.TealFunctionType
import com.galua.teal.psi.TealGenericParameter
import com.galua.teal.psi.TealIdentifierExpression
import com.galua.teal.psi.TealInterfaceDeclaration
import com.galua.teal.psi.TealIsClause
import com.galua.teal.psi.TealLocalDeclaration
import com.galua.teal.psi.TealMapType
import com.galua.teal.psi.TealNamedType
import com.galua.teal.psi.TealNumberLiteral
import com.galua.teal.psi.TealPsiElement
import com.galua.teal.psi.TealRecordDeclaration
import com.galua.teal.psi.TealRecordField
import com.galua.teal.psi.TealStringLiteral
import com.galua.teal.psi.TealTupleType
import com.galua.teal.psi.TealTypeIdentifier
import com.galua.teal.psi.TealTypeReference
import com.galua.teal.psi.TealWhereClause
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.TokenSet

class TealParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer = TealLexer()

    override fun createParser(project: Project?): PsiParser = TealParser()

    override fun getFileNodeType() = TealElementTypes.FILE

    override fun getWhitespaceTokens(): TokenSet = TokenSet.create(TokenType.WHITE_SPACE)

    override fun getCommentTokens(): TokenSet = TokenSet.create(TealTokenTypes.COMMENT)

    override fun getStringLiteralElements(): TokenSet = TokenSet.create(TealTokenTypes.STRING)

    override fun createElement(node: ASTNode): PsiElement =
        when (node.elementType) {
            TealElementTypes.LOCAL_DECLARATION -> TealLocalDeclaration(node)
            TealElementTypes.ENUM_DECLARATION -> TealEnumDeclaration(node)
            TealElementTypes.RECORD_DECLARATION -> TealRecordDeclaration(node)
            TealElementTypes.INTERFACE_DECLARATION -> TealInterfaceDeclaration(node)
            TealElementTypes.RECORD_FIELD -> TealRecordField(node)
            TealElementTypes.TYPE_REFERENCE -> TealTypeReference(node)
            TealElementTypes.TYPE_IDENTIFIER -> TealTypeIdentifier(node)
            TealElementTypes.NAMED_TYPE -> TealNamedType(node)
            TealElementTypes.GENERIC_PARAMETER -> TealGenericParameter(node)
            TealElementTypes.FUNCTION_TYPE -> TealFunctionType(node)
            TealElementTypes.MAP_TYPE -> TealMapType(node)
            TealElementTypes.ARRAY_TYPE -> TealArrayType(node)
            TealElementTypes.TUPLE_TYPE -> TealTupleType(node)
            TealElementTypes.WHERE_CLAUSE -> TealWhereClause(node)
            TealElementTypes.IS_CLAUSE -> TealIsClause(node)
            TealElementTypes.NUMBER_LITERAL -> TealNumberLiteral(node)
            TealElementTypes.STRING_LITERAL -> TealStringLiteral(node)
            TealElementTypes.BOOLEAN_LITERAL -> TealBooleanLiteral(node)
            TealElementTypes.IDENTIFIER -> TealIdentifierExpression(node)
            else -> TealPsiElement(node)
        }

    override fun createFile(viewProvider: FileViewProvider): PsiFile = TealFile(viewProvider)
}
