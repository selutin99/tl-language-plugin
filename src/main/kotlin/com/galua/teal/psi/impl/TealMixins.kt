/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.psi.impl

import com.galua.teal.psi.TealTypes
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner

open class TealStatMixin<T>(node: ASTNode) : ASTWrapperPsiElement(node)

open class TealNamedStubMixin<T>(node: ASTNode) : TealNamedElementMixin(node), PsiNameIdentifierOwner

open class TealNamedElementMixin(node: ASTNode) : ASTWrapperPsiElement(node), PsiNameIdentifierOwner {
    override fun getNameIdentifier(): PsiElement? = node.findChildByType(TealTypes.ID)?.psi

    override fun getName(): String? = nameIdentifier?.text

    override fun setName(name: String): PsiElement = this

    override fun getTextOffset(): Int = nameIdentifier?.textOffset ?: super.getTextOffset()
}

open class TealExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)

open class TealParenExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)

open class TealCallExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)

open class TealLiteralExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)

open class TealUnaryExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)

open class TealBinaryExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)

open class TealIndexExprMixin(node: ASTNode) : TealNamedElementMixin(node)

open class TealNameExprMixin(node: ASTNode) : TealNamedElementMixin(node)

open class TealClosureExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)

open class TealTableExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)
