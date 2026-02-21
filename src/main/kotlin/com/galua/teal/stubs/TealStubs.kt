/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.stubs

import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubBase
import com.intellij.psi.stubs.StubElement

open class TealStubBase<T : PsiElement>(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : StubBase<T>(parent, elementType)

interface TealExprStubElement<T : StubElement<*>> : PsiElement

class TealPlaceholderStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : TealStubBase<PsiElement>(parent, elementType)

class TealFuncStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : TealStubBase<PsiElement>(parent, elementType)

class TealClassMethodStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : TealStubBase<PsiElement>(parent, elementType)

class TealLocalFuncDefStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : TealStubBase<PsiElement>(parent, elementType)

class TealExprStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : TealStubBase<PsiElement>(parent, elementType)

class TealNameDefStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : TealStubBase<PsiElement>(parent, elementType)

class TealTableFieldStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : TealStubBase<PsiElement>(parent, elementType)

class TealBinaryExprStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : TealStubBase<PsiElement>(parent, elementType)

class TealUnaryExprStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : TealStubBase<PsiElement>(parent, elementType)

class TealIndexExprStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : TealStubBase<PsiElement>(parent, elementType)

class TealClosureExprStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : TealStubBase<PsiElement>(parent, elementType)

class TealTableExprStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : TealStubBase<PsiElement>(parent, elementType)

class TealNameExprStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : TealStubBase<PsiElement>(parent, elementType)

class TealLiteralExprStub(
    parent: StubElement<*>?,
    elementType: IStubElementType<*, *>,
) : TealStubBase<PsiElement>(parent, elementType)
