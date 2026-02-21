/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.psi

import com.galua.teal.parser.TealElementTypes
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

open class TealPsiElementBase(node: ASTNode) : ASTWrapperPsiElement(node)

class TealLocalDeclaration(node: ASTNode) : TealPsiElementBase(node) {
    fun typeReference(): TealTypeReference? = findChildByClass(TealTypeReference::class.java)

    fun initializer(): PsiElement? =
        children.firstOrNull { child ->
            child.node.elementType == TealElementTypes.NUMBER_LITERAL ||
                child.node.elementType == TealElementTypes.STRING_LITERAL ||
                child.node.elementType == TealElementTypes.BOOLEAN_LITERAL ||
                child.node.elementType == TealElementTypes.IDENTIFIER
        }
}

class TealEnumDeclaration(node: ASTNode) : TealPsiElementBase(node)

class TealRecordDeclaration(node: ASTNode) : TealPsiElementBase(node)

class TealInterfaceDeclaration(node: ASTNode) : TealPsiElementBase(node)

class TealRecordField(node: ASTNode) : TealPsiElementBase(node) {
    fun typeReference(): TealTypeReference? = findChildByClass(TealTypeReference::class.java)
}

class TealTypeReference(node: ASTNode) : TealPsiElementBase(node)

class TealTypeIdentifier(node: ASTNode) : TealPsiElementBase(node)

class TealNamedType(node: ASTNode) : TealPsiElementBase(node)

class TealGenericParameter(node: ASTNode) : TealPsiElementBase(node)

class TealFunctionType(node: ASTNode) : TealPsiElementBase(node)

class TealMapType(node: ASTNode) : TealPsiElementBase(node)

class TealArrayType(node: ASTNode) : TealPsiElementBase(node)

class TealTupleType(node: ASTNode) : TealPsiElementBase(node)

class TealWhereClause(node: ASTNode) : TealPsiElementBase(node)

class TealIsClause(node: ASTNode) : TealPsiElementBase(node)

class TealNumberLiteral(node: ASTNode) : TealPsiElementBase(node)

class TealStringLiteral(node: ASTNode) : TealPsiElementBase(node)

class TealBooleanLiteral(node: ASTNode) : TealPsiElementBase(node)

class TealIdentifierExpression(node: ASTNode) : TealPsiElementBase(node)
