/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.psi.impl

import com.galua.teal.stubs.TealExprStub
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement

open class TealStatMixin<T : StubElement<*>> : StubBasedPsiElementBase<T> {
    constructor(stub: T, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)
}

open class TealExprMixin : StubBasedPsiElementBase<TealExprStub> {
    constructor(stub: TealExprStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)
}

open class TealParenExprMixin : StubBasedPsiElementBase<TealExprStub> {
    constructor(stub: TealExprStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)
}

open class TealCallExprMixin : StubBasedPsiElementBase<TealExprStub> {
    constructor(stub: TealExprStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)
}

open class TealLiteralExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)

open class TealUnaryExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)

open class TealBinaryExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)

open class TealIndexExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)

open class TealNameExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)

open class TealClosureExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)

open class TealTableExprMixin(node: ASTNode) : ASTWrapperPsiElement(node)
