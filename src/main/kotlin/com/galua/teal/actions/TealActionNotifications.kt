/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.actions

import com.galua.teal.core.TealConstants.TEAL_NAME
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

/**
 * Sends Teal action notifications through the plugin notification group
 */
object TealActionNotifications {
    /**
     * Shows a notification with an explicit title, body, and notification type
     *
     * @param project project that receives the notification
     * @param title notification title shown in the IDE
     * @param content notification body shown in the IDE
     * @param type notification type used by IntelliJ
     */
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

    /**
     * Shows a warning notification under the Teal plugin title
     *
     * @param project project that receives the notification
     * @param message warning message shown in the IDE
     */
    fun warn(
        project: Project,
        message: String,
    ) {
        notify(project, TEAL_NAME, message, NotificationType.WARNING)
    }

    /**
     * Shows an informational notification under the Teal plugin title
     *
     * @param project project that receives the notification
     * @param message informational message shown in the IDE
     */
    fun info(
        project: Project,
        message: String,
    ) {
        notify(project, TEAL_NAME, message, NotificationType.INFORMATION)
    }

    /**
     * Shows an error notification with a custom title
     *
     * @param project project that receives the notification
     * @param title error title shown in the IDE
     * @param message error message shown in the IDE
     */
    fun error(
        project: Project,
        title: String,
        message: String,
    ) {
        notify(project, title, message, NotificationType.ERROR)
    }
}
