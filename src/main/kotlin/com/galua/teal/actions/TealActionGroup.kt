/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.actions

import com.galua.teal.core.TealConstants.TEAL_FILE_EXTENSION
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup

/**
 * Shows the Teal action group only for selected Teal source files
 */
class TealActionGroup : DefaultActionGroup() {
    /**
     * Updates group visibility from the currently selected virtual file
     *
     * @param event action event used to update group presentation state
     */
    override fun update(event: AnActionEvent) {
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabledAndVisible = file?.extension == TEAL_FILE_EXTENSION
    }
}
