/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.language.parser

import com.galua.teal.language.lexer.TealLexer
import com.galua.teal.language.lexer.TealTokenTypes
import com.galua.teal.language.psi.TealBooleanLiteral
import com.galua.teal.language.psi.TealFile
import com.galua.teal.language.psi.TealIdentifierExpression
import com.galua.teal.language.psi.TealLocalDeclaration
import com.galua.teal.language.psi.TealNumberLiteral
import com.galua.teal.language.psi.TealPsiElement
import com.galua.teal.language.psi.TealStringLiteral
import com.galua.teal.language.psi.TealTypeReference
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
            TealElementTypes.TYPE_REFERENCE -> TealTypeReference(node)
            TealElementTypes.NUMBER_LITERAL -> TealNumberLiteral(node)
            TealElementTypes.STRING_LITERAL -> TealStringLiteral(node)
            TealElementTypes.BOOLEAN_LITERAL -> TealBooleanLiteral(node)
            TealElementTypes.IDENTIFIER -> TealIdentifierExpression(node)
            else -> TealPsiElement(node)
        }

    override fun createFile(viewProvider: FileViewProvider): PsiFile = TealFile(viewProvider)
}
