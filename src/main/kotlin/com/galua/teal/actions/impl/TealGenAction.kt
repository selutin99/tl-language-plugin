/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.actions.impl

import com.galua.teal.actions.TealActionNotifications
import com.galua.teal.actions.TealFileAction
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task

/**
 * Runs tl gen for the selected Teal file and reports generation status
 */
class TealGenAction : TealFileAction() {
    /**
     * Starts tl gen in a background task for the selected Teal file
     *
     * @param event action event that provides project and selected file context
     */
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val file = super.selectedTealFile(event)
        if (file == null) {
            TealActionNotifications.warn(project, "Select a .tl file to run tl gen.")
            return
        }
        saveFileIfNeeded(file)

        object : Task.Backgroundable(project, "Running tl gen", false) {
            /**
             * Executes tl gen and reports success or failure through IDE notifications
             *
             * @param indicator background task progress indicator
             * @throws com.intellij.execution.ExecutionException when IntelliJ cannot start the process
             */
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
