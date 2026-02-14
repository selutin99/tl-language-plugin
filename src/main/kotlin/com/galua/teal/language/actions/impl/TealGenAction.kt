/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.language.actions.impl

import com.galua.teal.language.actions.TealActionNotifications
import com.galua.teal.language.actions.TealFileAction
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task

class TealGenAction : TealFileAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val file = super.selectedTealFile(event)
        if (file == null) {
            TealActionNotifications.warn(project, "Select a .tl file to run tl gen.")
            return
        }

        object : Task.Backgroundable(project, "Running tl gen", false) {
            override fun run(indicator: ProgressIndicator) {
                val commandLine =
                    GeneralCommandLine("tl", "gen", file.path)
                        .withWorkDirectory(file.parent.path)
                val processHandler = CapturingProcessHandler(commandLine)
                val output = processHandler.runProcess()

                if (output.exitCode == 0) {
                    TealActionNotifications.info(
                        project,
                        "Generated ${file.nameWithoutExtension}.lua",
                    )
                } else {
                    val details =
                        listOf(output.stderr, output.stdout)
                            .filter { it.isNotBlank() }
                            .joinToString("\n")
                            .ifBlank { "tl gen failed with exit code ${output.exitCode}." }
                    TealActionNotifications.notify(
                        project,
                        "Teal gen failed",
                        details.trim(),
                        NotificationType.ERROR,
                    )
                }
            }
        }.queue()
    }
}
