import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")

    id("com.github.johnrengelman.shadow").version("5.2.0")
    id("org.jlleitschuh.gradle.ktlint").version("9.2.1")
    id("com.github.ben-manes.versions").version("0.28.0")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
        kotlinOptions.javaParameters = true
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    named<ShadowJar>("shadowJar") {
        mergeServiceFiles()
    }
}

application {
    mainClassName = "com.albertattard.presentation.Application"
}

dependencies {
    val kotlin = "1.3.72"
    val slf4j = "2.0.0-alpha1"
    val logbackClassic = "1.3.0-alpha5"
    val jansi = "1.18"

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin")

    /* Logging */
    implementation("org.slf4j:slf4j-api:$slf4j")
    runtimeOnly("ch.qos.logback:logback-classic:$logbackClassic")
    runtimeOnly("org.fusesource.jansi:jansi:$jansi")
}

defaultTasks("clean", "ktlintFormat", "dependencyUpdates", "test")
