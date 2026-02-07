plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij")
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
