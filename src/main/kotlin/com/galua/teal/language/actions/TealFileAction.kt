/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.language.actions

import com.galua.teal.language.core.TealConstants.TEAL_FILE_EXTENSION
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile

abstract class TealFileAction : AnAction() {
    protected fun selectedTealFile(event: AnActionEvent): VirtualFile? {
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        return if (file?.extension == TEAL_FILE_EXTENSION) file else null
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = selectedTealFile(event) != null
    }
}
