pluginManagement {
    val jetbrainsIntellijVersion: String by settings
    val jetbrainsGrammarKitVersion: String by settings
    val kotlinVersion: String by settings
    val ktlintVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        id("org.jetbrains.intellij") version jetbrainsIntellijVersion
        id("org.jetbrains.grammarkit") version jetbrainsGrammarKitVersion
        id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
    }
}

rootProject.name = "tl-language-plugin"
