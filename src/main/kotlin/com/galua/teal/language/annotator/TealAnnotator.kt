/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.language.annotator

import com.galua.teal.language.lexer.TealTokenTypes
import com.galua.teal.language.parser.TealElementTypes
import com.galua.teal.language.psi.TealLocalDeclaration
import com.galua.teal.language.psi.TealTypeReference
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement

class TealAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is TealLocalDeclaration) {
            validateLocalDeclaration(element, holder)
        }
    }

    private fun validateLocalDeclaration(element: TealLocalDeclaration, holder: AnnotationHolder) {
        val typeReference = element.typeReference() ?: return
        val (declaredType, declaredTypeElement) = extractDeclaredType(typeReference)
        if (!BUILT_IN_TYPES.contains(declaredType)) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Unknown type '$declaredType'"
            ).range(declaredTypeElement ?: typeReference).create()
            return
        }

        val initializer = element.initializer() ?: return
        val initializerType = when (initializer.node.elementType) {
            TealElementTypes.NUMBER_LITERAL -> "number"
            TealElementTypes.STRING_LITERAL -> "string"
            TealElementTypes.BOOLEAN_LITERAL -> "boolean"
            else -> null
        } ?: return

        if (!isAssignable(declaredType, initializerType)) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                "Cannot assign $initializerType to $declaredType"
            ).range(initializer).create()
        }
    }

    private fun extractDeclaredType(typeReference: TealTypeReference): Pair<String, PsiElement?> {
        val typeElement = typeReference.children.firstOrNull { child ->
            child.node.elementType == TealTokenTypes.TYPE ||
                child.node.elementType == TealTokenTypes.IDENTIFIER
        }
        val declaredType = typeElement?.text?.trim()
        if (!declaredType.isNullOrEmpty()) {
            return declaredType to typeElement
        }

        val rawText = typeReference.text.trim()
        val strippedText = rawText.replaceFirst(Regex("^:+\\s*"), "").trim()
        return if (strippedText.isNotEmpty()) {
            strippedText to null
        } else {
            rawText to null
        }
    }

    private fun isAssignable(declaredType: String, initializerType: String): Boolean {
        if (declaredType == initializerType) {
            return true
        }
        return declaredType == "number" && initializerType == "integer"
    }

    companion object {

        private val BUILT_IN_TYPES = setOf(
            "any",
            "boolean",
            "integer",
            "number",
            "string",
            "table",
            "thread",
            "userdata",
            "function"
        )
    }
}
