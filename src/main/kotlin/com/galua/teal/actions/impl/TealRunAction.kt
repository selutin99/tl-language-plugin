/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.actions.impl

import com.galua.teal.actions.TealConsoleCommandAction

/**
 * Runs the selected Teal file through the Teal CLI in an IntelliJ run console
 */
class TealRunAction : TealConsoleCommandAction(command = "run")
