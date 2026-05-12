/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal

import java.nio.file.Path

object TestUtils {
    val testResourcesRoot: Path =
        Path.of("src/test/resources")
            .toAbsolutePath()
            .normalize()

    fun loadTestResource(path: String): String =
        requireNotNull(
            javaClass.classLoader.getResourceAsStream(path)
        ) {
            "Test resource not found: $path"
        }.bufferedReader()
            .use { it.readText() }

    fun testResourcePath(path: String): Path {
        loadTestResource(path)
        return testResourcesRoot.resolve(path)
    }
}
