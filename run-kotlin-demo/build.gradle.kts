import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm").version("1.3.72")

    id("com.github.johnrengelman.shadow").version("5.2.0")
    id("org.jlleitschuh.gradle.ktlint").version("9.2.1")
    id("com.github.ben-manes.versions").version("0.28.0")
}

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
}

val developmentOnly: Configuration by configurations.creating

configurations {
    all {
        resolutionStrategy {
            val ktlint = "0.36.0"
            force(
                "com.pinterest:ktlint:$ktlint",
                "com.pinterest.ktlint:ktlint-reporter-checkstyle:$ktlint"
            )
        }
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
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
    val logbackClassic = "1.3.0-alpha5"
    val jansi = "1.18"

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin")

    /* Logging */
    runtimeOnly("ch.qos.logback:logback-classic:$logbackClassic")
    runtimeOnly("org.fusesource.jansi:jansi:$jansi")
}

defaultTasks("clean", "ktlintFormat", "dependencyUpdates", "test")
