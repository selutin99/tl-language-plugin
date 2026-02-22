/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
@file:Suppress("UNUSED_PARAMETER")

package com.galua.teal.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.util.PsiTreeUtil

fun getComment(element: PsiElement): PsiComment? = PsiTreeUtil.findChildOfType(element, PsiComment::class.java)

fun assign(element: PsiElement): PsiElement? = element.node.findChildByType(TealTypes.ASSIGN)?.psi

fun varExprList(element: PsiElement): TealExprList? = PsiTreeUtil.getChildrenOfTypeAsList(element, TealExprList::class.java).firstOrNull()

fun valueExprList(element: PsiElement): TealExprList? = PsiTreeUtil.getChildrenOfTypeAsList(element, TealExprList::class.java).getOrNull(1)

fun until(element: PsiElement): PsiElement? = element.node.findChildByType(TealTypes.UNTIL)?.psi

fun getParamNameDefList(element: PsiElement): List<TealParamNameDef> =
    PsiTreeUtil.getChildrenOfTypeAsList(element, TealParamNameDef::class.java)

fun getNameIdentifier(element: PsiElement): PsiElement? = element.node.findChildByType(TealTypes.ID)?.psi

fun getTextOffset(element: PsiElement): Int = getNameIdentifier(element)?.textOffset ?: element.textOffset

fun setName(
    element: PsiElement,
    name: String,
): PsiElement = element

fun getName(element: PsiElement): String? = getNameIdentifier(element)?.text

fun getReferences(element: PsiElement): Array<PsiReference> = PsiReference.EMPTY_ARRAY

fun getPresentation(element: PsiElement): ItemPresentation? = null

fun toString(element: PsiElement): String = element.text

fun guessReturnType(element: PsiElement): PsiElement? = null

fun guessParentType(element: PsiElement): PsiElement? = null

fun getVisibility(element: PsiElement): String? = null

fun getWorth(element: PsiElement): Int = 0

fun isDeprecated(element: PsiElement): Boolean = false

fun getParams(element: PsiElement): List<TealParamDef> = PsiTreeUtil.getChildrenOfTypeAsList(element, TealParamDef::class.java)

fun isStatic(element: PsiElement): Boolean = false

fun dot(element: PsiElement): PsiElement? = element.node.findChildByType(TealTypes.DOT)?.psi

fun colon(element: PsiElement): PsiElement? = element.node.findChildByType(TealTypes.COLON)?.psi

fun getUseScope(element: PsiElement): SearchScope = element.containingFile?.useScope ?: GlobalSearchScope.EMPTY_SCOPE

fun guessTypeAt(element: PsiElement): PsiElement? = null

fun getIdExpr(element: PsiElement): PsiElement? = element.node.findChildByType(TealTypes.ID)?.psi

fun lbrack(element: PsiElement): PsiElement? = element.node.findChildByType(TealTypes.LBRACK)?.psi

fun getFirstStringArg(element: PsiElement): PsiElement? {
    val literal = PsiTreeUtil.findChildOfType(element, TealLiteralExpr::class.java) ?: return null
    return if (literal.node.findChildByType(TealTypes.STRING) != null) literal else null
}

fun isMethodDotCall(element: PsiElement): Boolean = false

fun isMethodColonCall(element: PsiElement): Boolean = false

fun isFunctionCall(element: PsiElement): Boolean = true

fun findField(element: PsiElement): TealTableField? = PsiTreeUtil.findChildOfType(element, TealTableField::class.java)

fun getFieldName(element: PsiElement): String? = PsiTreeUtil.findChildOfType(element, TealNameDef::class.java)?.name ?: getName(element)

fun nameDef(element: PsiElement): TealNameDef? = PsiTreeUtil.findChildOfType(element, TealNameDef::class.java)

@Suppress("ktlint:standard:function-naming")
fun RPAREN(element: PsiElement): PsiElement? = element.node.findChildByType(TealTypes.RPAREN)?.psi

@Suppress("ktlint:standard:function-naming")
fun ELLIPSIS(element: PsiElement): PsiElement? = element.node.findChildByType(TealTypes.ELLIPSIS)?.psi
