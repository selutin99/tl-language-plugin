/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.annotator

import com.galua.teal.highlighting.TealSyntaxHighlighter
import com.galua.teal.psi.TealNameDef
import com.galua.teal.psi.TealTypeName
import com.galua.teal.psi.TealTypeNameDef
import com.galua.teal.psi.TealTypes
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement

class TealAnnotator : Annotator {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (element.node.elementType != TealTypes.ID) {
            return
        }

        when {
            isTypeNameToken(element) -> highlightTypeIdentifier(element, holder)
            isNameDefinitionToken(element) -> highlightNameDefinition(element, holder)
        }
    }

    private fun highlightTypeIdentifier(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element)
            .textAttributes(TealSyntaxHighlighter.TYPE_IDENTIFIER_KEY)
            .create()
    }

    private fun highlightNameDefinition(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element)
            .textAttributes(TealSyntaxHighlighter.NAME_DEFINITION_KEY)
            .create()
    }

    private fun isTypeNameToken(element: PsiElement): Boolean {
        val parent = element.parent
        return parent is TealTypeNameDef ||
            parent is TealTypeName
    }

    private fun isNameDefinitionToken(element: PsiElement): Boolean = element.parent is TealNameDef
}
