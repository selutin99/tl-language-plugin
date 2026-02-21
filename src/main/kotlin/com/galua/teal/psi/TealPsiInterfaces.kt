/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.psi

import com.intellij.psi.PsiNamedElement

interface TealStatement : TealPsiElement

interface TealDeclaration : TealStatement

interface TealDeclarationScope : TealPsiElement

interface TealIndentRange : TealPsiElement

interface TealLoop : TealPsiElement

interface TealParametersOwner : TealPsiElement

interface TealFuncBodyOwner : TealPsiElement

interface TealClassMethod : TealPsiElement

interface TealClassMember : TealNamedElement

interface TealClassField : TealNamedElement

interface TealModuleClassField : TealPsiElement

interface TealNamedElement : PsiNamedElement

interface TealTypeGuessable : TealPsiElement

interface TealCommentOwner : TealPsiElement
