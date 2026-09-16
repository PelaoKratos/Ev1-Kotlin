plugins {
    kotlin("jvm") version "2.3.0"
}

group = "org.prueba"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin:kotlin-coroutines-core:1.10.2")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}