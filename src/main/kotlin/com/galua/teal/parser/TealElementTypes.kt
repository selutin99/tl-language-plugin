/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.parser

import com.galua.teal.core.TealLanguage
import com.intellij.psi.tree.IFileElementType

object TealElementTypes {
    val FILE = IFileElementType(TealLanguage)
    val LOCAL_DECLARATION = TealElementType("LOCAL_DECLARATION")
    val ENUM_DECLARATION = TealElementType("ENUM_DECLARATION")
    val ENUM_MEMBER = TealElementType("ENUM_MEMBER")
    val RECORD_DECLARATION = TealElementType("RECORD_DECLARATION")
    val INTERFACE_DECLARATION = TealElementType("INTERFACE_DECLARATION")
    val RECORD_FIELD = TealElementType("RECORD_FIELD")
    val TYPE_REFERENCE = TealElementType("TYPE_REFERENCE")
    val TYPE_IDENTIFIER = TealElementType("TYPE_IDENTIFIER")
    val NAMED_TYPE = TealElementType("NAMED_TYPE")
    val GENERIC_PARAMETER_LIST = TealElementType("GENERIC_PARAMETER_LIST")
    val GENERIC_PARAMETER = TealElementType("GENERIC_PARAMETER")
    val GENERIC_ARGUMENT_LIST = TealElementType("GENERIC_ARGUMENT_LIST")
    val FUNCTION_TYPE = TealElementType("FUNCTION_TYPE")
    val FUNCTION_TYPE_PARAM_LIST = TealElementType("FUNCTION_TYPE_PARAM_LIST")
    val FUNCTION_TYPE_RETURN_LIST = TealElementType("FUNCTION_TYPE_RETURN_LIST")
    val MAP_TYPE = TealElementType("MAP_TYPE")
    val ARRAY_TYPE = TealElementType("ARRAY_TYPE")
    val TUPLE_TYPE = TealElementType("TUPLE_TYPE")
    val WHERE_CLAUSE = TealElementType("WHERE_CLAUSE")
    val IS_CLAUSE = TealElementType("IS_CLAUSE")
    val IDENTIFIER = TealElementType("IDENTIFIER_EXPRESSION")
    val NUMBER_LITERAL = TealElementType("NUMBER_LITERAL")
    val STRING_LITERAL = TealElementType("STRING_LITERAL")
    val BOOLEAN_LITERAL = TealElementType("BOOLEAN_LITERAL")
}
