/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.parser

import com.galua.teal.core.TealLanguage
import com.intellij.psi.tree.IElementType

class TealElementType(debugName: String) : IElementType(debugName, TealLanguage)
