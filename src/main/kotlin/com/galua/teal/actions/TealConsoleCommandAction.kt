/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.actions

import com.galua.teal.core.TealConstants.TEAL_NAME
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.execution.ui.RunContentManager
import com.intellij.openapi.actionSystem.AnActionEvent

abstract class TealConsoleCommandAction(private val command: String) : TealFileAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val file = selectedTealFile(event)
        if (file == null) {
            TealActionNotifications.warn(project, "Select a .tl file to run tl $command.")
            return
        }
        saveFileIfNeeded(file)

        val console =
            TextConsoleBuilderFactory.getInstance().createBuilder(project).console
        val commandLine =
            GeneralCommandLine("tl", command, file.path)
                .withWorkDirectory(file.parent.path)
        val processHandler = OSProcessHandler(commandLine)
        console.attachToProcess(processHandler)
        ProcessTerminatedListener.attach(processHandler)

        val descriptor =
            RunContentDescriptor(
                console,
                processHandler,
                console.component,
                "$TEAL_NAME $command: ${file.name}",
            )
        RunContentManager.getInstance(project)
            .showRunContent(DefaultRunExecutor.getRunExecutorInstance(), descriptor)

        processHandler.startNotify()
    }
}
