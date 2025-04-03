import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.maven.publish)
}

version = libs.versions.mod.version.get()
group = libs.versions.maven.group.get()

base {
    archivesName.set(libs.versions.archives.base.name.get())
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
    minecraft(libs.minecraft)
    mappings("${libs.yarn.mappings.get()}:v2")
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.language.kotlin)
    modCompileOnly(libs.modmenu)
    modApi(libs.cloth.config.fabric) {
        exclude(group = "net.fabricmc.fabric-api")
    }
}

tasks.processResources {
    val propertyMap = mapOf(
        "version" to version,
        "minecraft_version" to libs.versions.minecraft.get(),
        "loader_version" to libs.versions.loader.get(),
        "kotlin_loader_version" to libs.versions.kotlin.loader.get(),
        "modmenu_version" to libs.versions.modmenu.get(),
        "cloth_config_version" to libs.versions.cloth.config.get(),
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
        rename { "${it}_${base.archivesName.get()}" }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = base.archivesName.get()
            from(components["java"])
        }
    }

    repositories { }
}
