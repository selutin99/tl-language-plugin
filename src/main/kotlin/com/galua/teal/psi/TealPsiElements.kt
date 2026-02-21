/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.psi

import com.galua.teal.parser.TealElementTypes
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

open class TealPsiElement(node: ASTNode) : ASTWrapperPsiElement(node)

class TealLocalDeclaration(node: ASTNode) : TealPsiElement(node) {
    fun typeReference(): TealTypeReference? = findChildByClass(TealTypeReference::class.java)

    fun initializer(): PsiElement? =
        children.firstOrNull { child ->
            child.node.elementType == TealElementTypes.NUMBER_LITERAL ||
                child.node.elementType == TealElementTypes.STRING_LITERAL ||
                child.node.elementType == TealElementTypes.BOOLEAN_LITERAL ||
                child.node.elementType == TealElementTypes.IDENTIFIER
        }
}

class TealEnumDeclaration(node: ASTNode) : TealPsiElement(node)

class TealRecordDeclaration(node: ASTNode) : TealPsiElement(node)

class TealInterfaceDeclaration(node: ASTNode) : TealPsiElement(node)

class TealRecordField(node: ASTNode) : TealPsiElement(node) {
    fun typeReference(): TealTypeReference? = findChildByClass(TealTypeReference::class.java)
}

class TealTypeReference(node: ASTNode) : TealPsiElement(node)

class TealTypeIdentifier(node: ASTNode) : TealPsiElement(node)

class TealNamedType(node: ASTNode) : TealPsiElement(node)

class TealGenericParameter(node: ASTNode) : TealPsiElement(node)

class TealFunctionType(node: ASTNode) : TealPsiElement(node)

class TealMapType(node: ASTNode) : TealPsiElement(node)

class TealArrayType(node: ASTNode) : TealPsiElement(node)

class TealTupleType(node: ASTNode) : TealPsiElement(node)

class TealWhereClause(node: ASTNode) : TealPsiElement(node)

class TealIsClause(node: ASTNode) : TealPsiElement(node)

class TealNumberLiteral(node: ASTNode) : TealPsiElement(node)

class TealStringLiteral(node: ASTNode) : TealPsiElement(node)

class TealBooleanLiteral(node: ASTNode) : TealPsiElement(node)

class TealIdentifierExpression(node: ASTNode) : TealPsiElement(node)
