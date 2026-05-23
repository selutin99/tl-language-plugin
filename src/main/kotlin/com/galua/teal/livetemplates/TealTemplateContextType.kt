/*
 * SPDX-FileCopyrightText: Copyright (c) 2026 Alexander Selyutin
 * SPDX-License-Identifier: MIT
 */
package com.galua.teal.livetemplates

import com.galua.teal.core.TealFileType
import com.intellij.codeInsight.template.FileTypeBasedContextType

class TealTemplateContextType : FileTypeBasedContextType("Teal", TealFileType)
