package com.galua.teal.language.parser

import com.galua.teal.language.core.TealLanguage
import com.intellij.psi.tree.IFileElementType

object TealElementTypes {
    val FILE = IFileElementType(TealLanguage)
    val LOCAL_DECLARATION = TealElementType("LOCAL_DECLARATION")
    val TYPE_REFERENCE = TealElementType("TYPE_REFERENCE")
    val IDENTIFIER = TealElementType("IDENTIFIER_EXPRESSION")
    val NUMBER_LITERAL = TealElementType("NUMBER_LITERAL")
    val STRING_LITERAL = TealElementType("STRING_LITERAL")
    val BOOLEAN_LITERAL = TealElementType("BOOLEAN_LITERAL")
}
