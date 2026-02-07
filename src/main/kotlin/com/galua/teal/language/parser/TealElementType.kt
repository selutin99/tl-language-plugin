/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.language.parser

import com.galua.teal.language.core.TealLanguage
import com.intellij.psi.tree.IElementType

class TealElementType(debugName: String) : IElementType(debugName, TealLanguage)
