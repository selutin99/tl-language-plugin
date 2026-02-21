/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.lang

import com.galua.teal.psi.TealElementType
import com.galua.teal.psi.TealTokenType
import com.intellij.psi.tree.IElementType

fun createType(debugName: String): IElementType = TealElementType(debugName)

fun createToken(debugName: String): IElementType = TealTokenType(debugName)
