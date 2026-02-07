pluginManagement {
    val kotlinVersion: String by settings
    val jetbrainsIntellijVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        id("org.jetbrains.intellij") version jetbrainsIntellijVersion
    }
}

rootProject.name = "tl-language-plugin"
