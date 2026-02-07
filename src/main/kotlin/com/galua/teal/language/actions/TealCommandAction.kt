package com.galua.teal.language.actions

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

abstract class TealCommandAction(
    private val command: String,
    private val successTitle: String,
    private val failureTitle: String
) : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        if (file == null || file.extension != "tl") {
            notify(project, "Teal", "Select a .tl file to run tl $command.", NotificationType.WARNING)
            return
        }

        object : Task.Backgroundable(project, "Running tl $command", false) {
            override fun run(indicator: ProgressIndicator) {
                val commandLine = GeneralCommandLine("tl", command, file.path)
                    .withWorkDirectory(file.parent.path)
                val processHandler = CapturingProcessHandler(commandLine)
                val output = processHandler.runProcess()

                if (output.exitCode == 0) {
                    val details = output.stdout.ifBlank { "Command completed successfully." }
                    notify(project, successTitle, details.trim(), NotificationType.INFORMATION)
                } else {
                    val details = listOf(output.stderr, output.stdout)
                        .filter { it.isNotBlank() }
                        .joinToString("\n")
                        .ifBlank { "Command failed with exit code ${output.exitCode}." }
                    notify(project, failureTitle, details.trim(), NotificationType.ERROR)
                }
            }
        }.queue()
    }

    private fun notify(project: Project, title: String, content: String, type: NotificationType) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("Teal")
            .createNotification(title, content, type)
            .notify(project)
    }
}

class TealCheckAction : TealCommandAction(
    command = "check",
    successTitle = "Teal check succeeded",
    failureTitle = "Teal check failed"
)

class TealGenAction : TealCommandAction(
    command = "gen",
    successTitle = "Teal gen succeeded",
    failureTitle = "Teal gen failed"
)

class TealRunAction : TealCommandAction(
    command = "run",
    successTitle = "Teal run succeeded",
    failureTitle = "Teal run failed"
)
