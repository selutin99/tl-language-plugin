plugins {
    kotlin("jvm") version "1.9.24"
    id("org.jetbrains.intellij") version "1.17.3"
}

group = "com.galua.teal"
version = "0.1.0"

repositories {
    mavenCentral()
}

intellij {
    version.set("2023.3")
    type.set("IC")
}

kotlin {
    jvmToolchain(17)
}
