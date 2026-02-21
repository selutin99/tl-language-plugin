/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.annotator

import com.galua.teal.highlighting.TealSyntaxHighlighter
import com.galua.teal.psi.TealExpr
import com.galua.teal.psi.TealLiteralExpr
import com.galua.teal.psi.TealLocalDef
import com.galua.teal.psi.TealTypeAnn
import com.galua.teal.psi.TealTypeName
import com.galua.teal.psi.TealTypes
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement

class TealAnnotator : Annotator {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        when (element) {
            is TealLocalDef -> validateLocalDeclaration(element, holder)
            is TealTypeName -> highlightTypeIdentifier(element, holder)
        }
    }

    private fun validateLocalDeclaration(
        element: TealLocalDef,
        holder: AnnotationHolder,
    ) {
        val typedNameList = element.typedNameList ?: return
        val exprList = element.exprList ?: return
        val exprs = exprList.exprList
        if (exprs.isEmpty()) return

        val typedNames = typedNameList.typedNameDefList
        for (index in 0 until minOf(typedNames.size, exprs.size)) {
            val declaredType = extractDeclaredType(typedNames[index].typeAnn) ?: continue
            if (!BUILT_IN_TYPES.contains(declaredType)) {
                continue
            }

            val initializer = exprs[index]
            val initializerType = inferLiteralType(initializer) ?: continue
            if (!isAssignable(declaredType, initializerType)) {
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    "Cannot assign $initializerType to $declaredType",
                ).range(initializer).create()
            }
        }
    }

    private fun extractDeclaredType(typeAnn: TealTypeAnn): String? {
        val typeText = typeAnn.typeexp?.text ?: typeAnn.text
        val stripped = typeText.replaceFirst(Regex("^:+\\s*"), "").trim()
        return stripped.ifEmpty { null }
    }

    private fun isAssignable(
        declaredType: String,
        initializerType: String,
    ): Boolean {
        if (declaredType == initializerType) {
            return true
        }
        return declaredType == "number" && initializerType == "integer"
    }

    private fun highlightTypeIdentifier(
        element: TealTypeName,
        holder: AnnotationHolder,
    ) {
        if (element.id == null) return
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element)
            .textAttributes(TealSyntaxHighlighter.TYPE_IDENTIFIER_KEY)
            .create()
    }

    private fun inferLiteralType(expr: TealExpr): String? {
        val literal = PsiTreeUtil.findChildOfType(expr, TealLiteralExpr::class.java) ?: return null
        val node = literal.node
        return when {
            node.findChildByType(TealTypes.NUMBER) != null -> "number"
            node.findChildByType(TealTypes.STRING) != null -> "string"
            node.findChildByType(TealTypes.TRUE) != null ||
                node.findChildByType(TealTypes.FALSE) != null -> "boolean"
            else -> null
        }
    }

    companion object {
        private val BUILT_IN_TYPES =
            setOf(
                "any",
                "boolean",
                "integer",
                "nil",
                "number",
                "string",
                "table",
                "thread",
                "userdata",
                "function",
            )
    }
}
