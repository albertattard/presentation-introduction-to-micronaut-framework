import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm").version("1.3.72")
    kotlin("kapt").version("1.3.72")
    kotlin("plugin.allopen").version("1.3.72")

    id("com.github.johnrengelman.shadow").version("5.2.0")
    id("org.jlleitschuh.gradle.ktlint").version("9.2.1")
    id("com.github.ben-manes.versions").version("0.28.0")
}

repositories {
    mavenLocal()
    jcenter()
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
    mainClassName = "com.albertattard.presentation.micronaut.Application"
}

allOpen {
    annotation("io.micronaut.aop.Around")
}

dependencies {
    val kotlin = "1.3.72"
    val micronaut = "1.3.2"
    val jna = "5.5.0"
    val directoryWatcher = "0.9.9"
    val jacksonModuleKotlin = "2.10.2"
    val logbackClassic = "1.2.3"
    val mockk = "1.9.3"
    val kotlintest = "1.1.5"
    val kotlintestRunner = "3.4.0"

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin")

    implementation(platform("io.micronaut:micronaut-bom:$micronaut"))
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-management")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonModuleKotlin")
    runtimeOnly("ch.qos.logback:logback-classic:$logbackClassic")

    kapt(platform("io.micronaut:micronaut-bom:$micronaut"))
    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut:micronaut-validation")
    kaptTest(platform("io.micronaut:micronaut-bom:$micronaut"))
    kaptTest("io.micronaut:micronaut-inject-java")

    /* Configuring Native File Watch on Mac OS X */
    developmentOnly("io.micronaut:micronaut-runtime-osx")
    developmentOnly("net.java.dev.jna:jna:$jna")
    developmentOnly("io.methvin:directory-watcher:$directoryWatcher")

    testImplementation(platform("io.micronaut:micronaut-bom:$micronaut"))
    testImplementation("io.micronaut.test:micronaut-test-kotlintest:$kotlintest")
    testImplementation("io.mockk:mockk:$mockk")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:$kotlintestRunner")
}

defaultTasks("clean", "ktlintFormat", "dependencyUpdates", "test")
