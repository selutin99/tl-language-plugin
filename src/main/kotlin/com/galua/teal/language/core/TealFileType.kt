package com.galua.teal.language.core

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object TealFileType : LanguageFileType(TealLanguage) {

    override fun getName(): String = "Teal"

    override fun getDescription(): String = "Teal language file"

    override fun getDefaultExtension(): String = "tl"

    override fun getIcon(): Icon = TealIcons.FILE
}
