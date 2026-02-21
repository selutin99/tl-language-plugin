/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.psi

import com.galua.teal.core.TealFileType
import com.galua.teal.core.TealLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

class TealFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, TealLanguage) {
    override fun getFileType() = TealFileType

    override fun toString(): String = "Teal File"
}
