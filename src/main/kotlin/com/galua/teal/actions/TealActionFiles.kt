/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.actions

import com.galua.teal.core.TealConstants.TEAL_FILE_EXTENSION
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFile

/**
 * Resolves a Teal file from action contexts used by editor and project view popups
 *
 * @param event action event that may contain virtual file, psi file, or project context
 * @return selected Teal file or null when the action context does not point to a .tl file
 */
internal fun findSelectedTealFile(event: AnActionEvent): VirtualFile? {
    event.getData(CommonDataKeys.VIRTUAL_FILE)?.let { return it.asTealFile() }
    event.getData(CommonDataKeys.PSI_FILE)?.virtualFile?.let { return it.asTealFile() }

    val project = event.project ?: return null
    return FileEditorManager.getInstance(project)
        .selectedFiles
        .firstNotNullOfOrNull(VirtualFile::asTealFile)
}

private fun VirtualFile.asTealFile(): VirtualFile? = takeIf { !it.isDirectory && it.extension == TEAL_FILE_EXTENSION }
