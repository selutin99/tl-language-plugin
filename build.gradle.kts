plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
    id("org.jetbrains.grammarkit")
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

sourceSets {
    main {
        java.srcDirs(
            "src/main/gen"
        )
    }
}

intellij {
    version.set("2023.3")
    type.set("IC")
}

grammarKit {
    jflexRelease.set("1.9.1")
}

ktlint {
    version.set("1.2.1")
    verbose.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}

sourceSets {
    main {
        java.srcDir(layout.buildDirectory.dir("generated-src"))
    }
}

tasks.named("compileKotlin") {
    dependsOn("generateParser")
}
