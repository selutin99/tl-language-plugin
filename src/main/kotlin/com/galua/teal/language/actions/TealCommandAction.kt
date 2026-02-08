/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.language.actions

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.execution.ui.RunContentManager
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

// TODO refactor this action, decompose & simplify
class TealCheckAction : TealConsoleCommandAction(command = "check")

class TealRunAction : TealConsoleCommandAction(command = "run")

class TealGenAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        if (file == null || file.extension != "tl") {
            notify(project, "Teal", "Select a .tl file to run tl gen.", NotificationType.WARNING)
            return
        }

        object : Task.Backgroundable(project, "Running tl gen", false) {
            override fun run(indicator: ProgressIndicator) {
                val commandLine = GeneralCommandLine("tl", "gen", file.path)
                    .withWorkDirectory(file.parent.path)
                val processHandler = CapturingProcessHandler(commandLine)
                val output = processHandler.runProcess()

                if (output.exitCode == 0) {
                    notify(
                        project,
                        "Teal",
                        "Generated ${file.nameWithoutExtension}.lua",
                        NotificationType.INFORMATION
                    )
                } else {
                    val details = listOf(output.stderr, output.stdout)
                        .filter { it.isNotBlank() }
                        .joinToString("\n")
                        .ifBlank { "tl gen failed with exit code ${output.exitCode}." }
                    notify(project, "Teal gen failed", details.trim(), NotificationType.ERROR)
                }
            }
        }.queue()
    }

    override fun update(event: AnActionEvent) {
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabledAndVisible = file?.extension == "tl"
    }

    private fun notify(project: Project, title: String, content: String, type: NotificationType) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Teal")
            .createNotification(title, content, type)
            .notify(project)
    }
}

abstract class TealConsoleCommandAction(private val command: String) : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        if (file == null || file.extension != "tl") {
            notify(project, "Teal", "Select a .tl file to run tl $command.", NotificationType.WARNING)
            return
        }

        val console = TextConsoleBuilderFactory.getInstance().createBuilder(project).console
        val commandLine = GeneralCommandLine("tl", command, file.path)
            .withWorkDirectory(file.parent.path)
        val processHandler = OSProcessHandler(commandLine)
        console.attachToProcess(processHandler)
        ProcessTerminatedListener.attach(processHandler)

        val descriptor = RunContentDescriptor(
            console,
            processHandler,
            console.component,
            "Teal $command: ${file.name}"
        )
        RunContentManager.getInstance(project)
            .showRunContent(DefaultRunExecutor.getRunExecutorInstance(), descriptor)

        processHandler.startNotify()
    }

    override fun update(event: AnActionEvent) {
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabledAndVisible = file?.extension == "tl"
    }

    private fun notify(project: Project, title: String, content: String, type: NotificationType) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Teal")
            .createNotification(title, content, type)
            .notify(project)
    }
}
