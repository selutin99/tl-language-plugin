package com.galua.teal.language.psi

import com.galua.teal.language.core.TealFileType
import com.galua.teal.language.core.TealLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

class TealFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, TealLanguage) {
    override fun getFileType() = TealFileType

    override fun toString(): String = "Teal File"
}
