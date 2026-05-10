/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal

object TestUtils {
    fun loadTestResource(path: String): String =
        requireNotNull(
            javaClass.classLoader.getResourceAsStream(path)
        ) {
            "Test resource not found: $path"
        }.bufferedReader()
            .use { it.readText() }
}
