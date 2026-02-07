/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.language.psi

import com.galua.teal.language.parser.TealElementTypes
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

open class TealPsiElement(node: ASTNode) : ASTWrapperPsiElement(node)

class TealLocalDeclaration(node: ASTNode) : TealPsiElement(node) {
    fun typeReference(): TealTypeReference? = findChildByClass(TealTypeReference::class.java)
    fun initializer(): PsiElement? = children.firstOrNull { child ->
        child.node.elementType == TealElementTypes.NUMBER_LITERAL ||
            child.node.elementType == TealElementTypes.STRING_LITERAL ||
            child.node.elementType == TealElementTypes.BOOLEAN_LITERAL ||
            child.node.elementType == TealElementTypes.IDENTIFIER
    }
}

class TealTypeReference(node: ASTNode) : TealPsiElement(node)

class TealNumberLiteral(node: ASTNode) : TealPsiElement(node)

class TealStringLiteral(node: ASTNode) : TealPsiElement(node)

class TealBooleanLiteral(node: ASTNode) : TealPsiElement(node)

class TealIdentifierExpression(node: ASTNode) : TealPsiElement(node)
