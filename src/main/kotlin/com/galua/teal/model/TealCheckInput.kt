/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.model

import java.nio.file.Path

data class TealCheckInput(
    val text: String,
    val originalFilePath: Path?,
    val workDirectory: Path,
)
