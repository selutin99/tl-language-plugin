/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.model

import java.nio.file.Path

/**
 * Immutable editor snapshot passed from IntelliJ to the background tl check run
 *
 * @param text current file text to validate with the Teal compiler
 * @param originalFilePath path of the opened file when it is backed by a virtual file
 * @param workDirectory directory used to resolve tlconfig.lua and relative compiler output paths
 */
data class TealCheckInput(
    val text: String,
    val originalFilePath: Path?,
    val workDirectory: Path,
)
