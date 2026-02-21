/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.parser

import com.galua.teal.lexer.TealLexer
import com.galua.teal.lexer.TealTokenTypes
import com.galua.teal.psi.TealFile
import com.galua.teal.psi.TealTypes
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

    override fun getFileNodeType() = TealFileElementType

    override fun getWhitespaceTokens(): TokenSet = TokenSet.create(TokenType.WHITE_SPACE)

    override fun getCommentTokens(): TokenSet = TokenSet.create(TealTokenTypes.COMMENT)

    override fun getStringLiteralElements(): TokenSet = TokenSet.create(TealTokenTypes.STRING)

    override fun createElement(node: ASTNode): PsiElement = TealTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = TealFile(viewProvider)
}
