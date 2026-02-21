/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.actions

import com.galua.teal.core.TealConstants.TEAL_NAME
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object TealActionNotifications {
    fun notify(
        project: Project,
        title: String,
        content: String,
        type: NotificationType,
    ) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup(TEAL_NAME)
            .createNotification(title, content, type)
            .notify(project)
    }

    fun warn(
        project: Project,
        message: String,
    ) {
        notify(project, TEAL_NAME, message, NotificationType.WARNING)
    }

    fun info(
        project: Project,
        message: String,
    ) {
        notify(project, TEAL_NAME, message, NotificationType.INFORMATION)
    }

    fun error(
        project: Project,
        title: String,
        message: String,
    ) {
        notify(project, title, message, NotificationType.ERROR)
    }
}
