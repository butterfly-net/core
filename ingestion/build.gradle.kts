import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val javalin_version: String by project
val slf4j_version: String by project
val jackson_version: String by project
val pg_version: String by project
val hikari_version: String by project

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "it.unibz.butterfly_net.core.ingestion"
version = "1.0-SNAPSHOT"

tasks.withType<ShadowJar> {
    archiveFileName = "${project.group}_${project.version}_shadow.jar"
    manifest {
        attributes["Main-Class"] = "${project.group}.Component"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":utils"))

    implementation("io.javalin:javalin:$javalin_version")
    implementation("org.slf4j:slf4j-simple:$slf4j_version")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jackson_version")
    implementation("org.postgresql:postgresql:$pg_version")
    implementation("com.zaxxer:HikariCP:$hikari_version")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {
    test {
        useJUnitPlatform()
    }
}