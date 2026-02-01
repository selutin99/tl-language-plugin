package com.galua.teal.language

import com.intellij.psi.tree.IElementType

class TealTokenType(debugName: String) : IElementType(debugName, TealLanguage)

object TealTokenTypes {

    val KEYWORD = TealTokenType("KEYWORD")
    val IDENTIFIER = TealTokenType("IDENTIFIER")
    val NUMBER = TealTokenType("NUMBER")
    val STRING = TealTokenType("STRING")
    val COMMENT = TealTokenType("COMMENT")
}
