/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.actions

import com.galua.teal.core.TealConstants.TEAL_FILE_EXTENSION
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFile

/**
 * Base action for commands that operate on the currently selected Teal file
 */
abstract class TealFileAction : AnAction() {
    /**
     * Saves an unsaved editor document before an external Teal CLI command reads it
     *
     * @param file virtual file whose document should be saved when needed
     */
    protected fun saveFileIfNeeded(file: VirtualFile) {
        val documentManager = FileDocumentManager.getInstance()
        val document = documentManager.getDocument(file) ?: return
        if (documentManager.isDocumentUnsaved(document)) {
            documentManager.saveDocument(document)
        }
    }

    /**
     * Resolves the selected virtual file when it is a Teal source file
     *
     * @param event action event that contains the current selection
     * @return selected Teal file or null when the selection is not a .tl file
     */
    protected fun selectedTealFile(event: AnActionEvent): VirtualFile? {
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        return if (file?.extension == TEAL_FILE_EXTENSION) file else null
    }

    /**
     * Shows the action only when a Teal file is selected
     *
     * @param event action event used to update presentation state
     */
    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = selectedTealFile(event) != null
    }
}
