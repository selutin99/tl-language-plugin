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
        java.srcDirs("src/main/gen")
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junitVersion")}")
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

tasks {
    patchPluginXml {
        sinceBuild.set("233")
        untilBuild.set("251.*")
    }

    withType<org.jetbrains.grammarkit.tasks.GenerateParserTask> {
        sourceFile.set(file("src/main/grammars/teal.bnf"))
        targetRootOutputDir.set(file("src/main/gen"))

        pathToParser.set("src/main/gen/com/galua/teal/parser/TealParser.java")
        pathToPsiRoot.set("src/main/gen/com/galua/teal/psi")
        purgeOldFiles.set(true)
    }

    withType<org.jetbrains.grammarkit.tasks.GenerateLexerTask> {
        sourceFile.set(file("src/main/grammars/_TealLexer.flex"))
        targetOutputDir.set(file("src/main/gen/com/galua/teal/lexer"))
        purgeOldFiles.set(true)
    }

    withType<org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask> {
        setSource(files("src/main/kotlin"))
    }
}

tasks.named("compileKotlin") {
    dependsOn("generateParser", "generateLexer")
}

tasks.register("printPluginVersion") {
    group = "help"
    description = "Prints the plugin version"

    doLast {
        println(project.version)
    }
}
