val javalin_version: String by project
val slf4j_version: String by project
val jackson_version: String by project

plugins {
    id("java")
}

group = "it.unibz.butterfly_net"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:$javalin_version")
    implementation("org.slf4j:slf4j-simple:$slf4j_version")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jackson_version")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}