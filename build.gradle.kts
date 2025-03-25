import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
    id("fabric-loom") version "1.10-SNAPSHOT"
    id("maven-publish")
    id("com.diffplug.spotless") version "7.0.2"
}

spotless {
    kotlin {
        ktlint()
    }

    java {
        importOrder()
        removeUnusedImports()
    }
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

val targetJavaVersion = 21
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    withSourcesJar()
}

sourceSets {
    main {
        java.srcDirs("src/main/kotlin")
        kotlin.srcDirs("src/main/kotlin")
    }
}

repositories {
    maven("https://maven.terraformersmc.com/")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.property("kotlin_loader_version")}")
    modCompileOnly("com.terraformersmc:modmenu:${project.property("modmenu_version")}")
    modApi("me.shedaniel.cloth:cloth-config-fabric:${project.property("cloth_config_version")}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
}

tasks.processResources {
    val propertyMap = mapOf(
        "version" to project.version,
        "minecraft_version" to project.property("minecraft_version"),
        "loader_version" to project.property("loader_version"),
        "kotlin_loader_version" to project.property("kotlin_loader_version"),
        "modmenu_version" to project.property("modmenu_version"),
        "cloth_config_version" to project.property("cloth_config_version")
    )
    inputs.properties(propertyMap)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(propertyMap)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName}" }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }
    }

    repositories { }
}
