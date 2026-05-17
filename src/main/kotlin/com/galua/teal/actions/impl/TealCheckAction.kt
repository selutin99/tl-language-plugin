/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.actions.impl

import com.galua.teal.actions.TealConsoleCommandAction

/**
 * Runs tl check for the selected Teal file in an IntelliJ run console
 */
class TealCheckAction : TealConsoleCommandAction(command = "check")
