/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.actions

import com.galua.teal.core.TealIcons
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup

/**
 * Shows the Teal action group only for selected Teal source files
 */
class TealActionGroup : DefaultActionGroup() {
    init {
        templatePresentation.icon = TealIcons.FILE
    }

    /**
     * Updates group visibility from the currently selected virtual file
     *
     * @param event action event used to update group presentation state
     */
    override fun update(event: AnActionEvent) {
        event.presentation.icon = TealIcons.FILE
        event.presentation.isEnabledAndVisible = findSelectedTealFile(event) != null
    }
}
