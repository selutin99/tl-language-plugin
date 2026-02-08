/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.language.core

import com.galua.teal.language.core.TealConstants.TEAL_FILE_EXTENSION
import com.galua.teal.language.core.TealConstants.TEAL_NAME
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object TealFileType : LanguageFileType(TealLanguage) {

    override fun getName(): String = TEAL_NAME

    override fun getDescription(): String = "Teal language file"

    override fun getDefaultExtension(): String = TEAL_FILE_EXTENSION

    override fun getIcon(): Icon = TealIcons.FILE
}
