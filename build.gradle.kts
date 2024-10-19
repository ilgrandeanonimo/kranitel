import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("io.freefair.lombok") version "8.10"
    id("io.github.goooler.shadow") version "8.1.7"
    id("io.papermc.paperweight.userdev") version "1.7.3"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("java")
}

group = "it.pboglione"
version = "1.0.0-alpha1"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "gradle-plugins"
        url = uri("https://plugins.gradle.org/m2/")
    }
    maven {
        name = "william278"
        url = uri("https://repo.william278.net/releases")
    }
}

dependencies {
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
    implementation("net.william278.uniform:uniform-paper:1.2.1")
    implementation("de.exlll:configlib-yaml:4.5.0")
}

java {
    toolchain.languageVersion.set(
        JavaLanguageVersion.of(21))
}

tasks.withType<ShadowJar> {
    archiveFileName = "kranitel-${version}.jar"

    relocate("de.exlll.configlib", "it.pboglione.dependencies.configlib")
    relocate("net.william278.uniform", "it.pboglione.dependencies.uniform")
    relocate("org.snakeyml", "it.pboglione.dependencies.snakeyml")

    minimize()
}