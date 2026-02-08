plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
    id("org.jlleitschuh.gradle.ktlint")
}

group = "com.galua.teal"
version = "0.1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

intellij {
    version.set("2023.3")
    type.set("IC")
}

ktlint {
    version.set("1.2.1")
    verbose.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}
