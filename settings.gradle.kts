pluginManagement {
    val jetbrainsIntellijVersion: String by settings
    val kotlinVersion: String by settings
    val ktlintVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        id("org.jetbrains.intellij") version jetbrainsIntellijVersion
        id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
    }
}

rootProject.name = "tl-language-plugin"
