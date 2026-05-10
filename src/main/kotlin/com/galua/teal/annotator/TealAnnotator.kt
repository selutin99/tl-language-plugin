/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.annotator

import com.galua.teal.highlighting.TealSyntaxHighlighter
import com.galua.teal.psi.TealBinaryExpr
import com.galua.teal.psi.TealExpr
import com.galua.teal.psi.TealLiteralExpr
import com.galua.teal.psi.TealLocalDef
import com.galua.teal.psi.TealTypeAnn
import com.galua.teal.psi.TealTypeName
import com.galua.teal.psi.TealTypeNameDef
import com.galua.teal.psi.TealTypedVarStat
import com.galua.teal.psi.TealTypeexp
import com.galua.teal.psi.TealTypes
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiTreeUtil

class TealAnnotator : Annotator {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (element.node.elementType == TealTypes.ID && isTypeNameToken(element)) {
            annotateTypeNameToken(element, holder)
            return
        }

        when (element) {
            is TealBinaryExpr -> validateBinaryExpression(element, holder)
            is TealLocalDef -> validateLocalDeclaration(element, holder)
            is TealTypedVarStat -> validateTypedVarStat(element, holder)
        }
    }

    private fun validateLocalDeclaration(
        element: TealLocalDef,
        holder: AnnotationHolder,
    ) {
        val typeListAnn = element.typeListAnn ?: return
        val exprList = element.exprList ?: return
        val exprs = exprList.exprList
        if (exprs.isEmpty()) return

        val declaredTypes = typeListAnn.typeexpList?.typeexpList ?: return
        for (index in 0 until minOf(declaredTypes.size, exprs.size)) {
            validateAssignment(declaredTypes[index], exprs[index], holder)
        }
    }

    private fun extractDeclaredType(typeAnn: TealTypeAnn): String? {
        val typeText = typeAnn.typeexp?.text ?: typeAnn.text
        return extractDeclaredType(typeText)
    }

    private fun extractDeclaredType(typeexp: TealTypeexp): String? = extractDeclaredType(typeexp.text)

    private fun extractDeclaredType(typeText: String): String? {
        val stripped = typeText.replaceFirst(Regex("^:+\\s*"), "").trim()
        return stripped.ifEmpty { null }
    }

    private fun validateTypedVarStat(
        element: TealTypedVarStat,
        holder: AnnotationHolder,
    ) {
        val exprList = element.exprList ?: return
        val exprs = exprList.exprList
        if (exprs.isEmpty()) return

        val typedVars = element.typedVarList.typedVarList
        for (index in 0 until minOf(typedVars.size, exprs.size)) {
            val declaredType = extractDeclaredType(typedVars[index].typeAnn) ?: continue
            if (!BUILT_IN_TYPES.contains(declaredType)) {
                continue
            }

            validateAssignment(declaredType, exprs[index], holder)
        }
    }

    private fun validateAssignment(
        typeexp: TealTypeexp,
        initializer: TealExpr,
        holder: AnnotationHolder,
    ) {
        val declaredType = extractDeclaredType(typeexp) ?: return
        if (!BUILT_IN_TYPES.contains(declaredType)) {
            return
        }

        validateAssignment(declaredType, initializer, holder)
    }

    private fun validateAssignment(
        declaredType: String,
        initializer: TealExpr,
        holder: AnnotationHolder,
    ) {
        val initializerType = inferLiteralType(initializer)?.name ?: return
        if (isAssignable(declaredType, initializerType)) {
            return
        }

        holder.newAnnotation(
            HighlightSeverity.ERROR,
            "Cannot assign $initializerType to $declaredType",
        ).range(initializer).create()
    }

    private fun validateBinaryExpression(
        element: TealBinaryExpr,
        holder: AnnotationHolder,
    ) {
        val operator = element.node.findChildByType(TealTypes.PLUS)?.psi ?: return
        val operands = element.children.filterIsInstance<TealExpr>()
        if (operands.size < 2) return

        val leftType = inferLiteralType(operands.first()) ?: return
        val rightType = inferLiteralType(operands.last()) ?: return
        if (leftType.name == "number" && rightType.name == "number") {
            return
        }

        holder.newAnnotation(
            HighlightSeverity.ERROR,
            "cannot use operator '${operator.text}' for types ${leftType.displayText} and ${rightType.displayText}",
        ).range(operator).create()
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
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element)
            .textAttributes(TealSyntaxHighlighter.TYPE_IDENTIFIER_KEY)
            .create()
    }

    private fun annotateTypeNameToken(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        highlightTypeIdentifier(element, holder)
        if (element.parent is TealTypeNameDef) {
            return
        }

        val typeName = element.text
        if (isKnownTypeName(typeName, element)) {
            return
        }

        holder.newAnnotation(
            HighlightSeverity.ERROR,
            "Unknown type: $typeName",
        ).range(element).create()
    }

    private fun isTypeNameToken(element: PsiElement): Boolean {
        val parent = element.parent
        return parent is TealTypeNameDef ||
            parent is TealTypeName
    }

    private fun isKnownTypeName(
        typeName: String,
        context: PsiElement,
    ): Boolean {
        if (BUILT_IN_TYPES.contains(typeName)) {
            return true
        }

        val file = context.containingFile ?: return false
        return getDeclaredTypeNames(file).contains(typeName)
    }

    private fun getDeclaredTypeNames(file: PsiFile): Set<String> =
        CachedValuesManager.getCachedValue(file) {
            val names =
                PsiTreeUtil.findChildrenOfType(file, TealTypeNameDef::class.java)
                    .mapNotNull { it.text?.takeIf(String::isNotBlank) }
                    .toSet()
            CachedValueProvider.Result.create(names, file)
        }

    private fun inferLiteralType(expr: TealExpr): LiteralType? {
        val literal =
            expr as? TealLiteralExpr
                ?: PsiTreeUtil.findChildOfType(expr, TealLiteralExpr::class.java)
                ?: return null
        val node = literal.node
        return when {
            node.findChildByType(TealTypes.NUMBER) != null ->
                LiteralType("number", node.findChildByType(TealTypes.NUMBER)?.text)
            node.findChildByType(TealTypes.STRING) != null ->
                LiteralType("string", node.findChildByType(TealTypes.STRING)?.text)
            node.findChildByType(TealTypes.TRUE) != null ||
                node.findChildByType(TealTypes.FALSE) != null -> LiteralType("boolean", null)
            else -> null
        }
    }

    private data class LiteralType(
        val name: String,
        private val literalText: String?,
    ) {
        val displayText: String = listOfNotNull(name, literalText).joinToString(" ")
    }

    companion object {
        private val BUILT_IN_TYPES =
            setOf(
                "any",
                "boolean",
                "integer",
                "nil",
                "number",
                "self",
                "string",
                "thread",
                "userdata",
            )
    }
}
