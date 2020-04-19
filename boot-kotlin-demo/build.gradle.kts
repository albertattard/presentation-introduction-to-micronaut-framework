plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "1.3.71"

    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"

    id("com.github.johnrengelman.shadow").version("5.2.0")
    id("org.jlleitschuh.gradle.ktlint").version("9.2.1")
    id("com.github.ben-manes.versions").version("0.28.0")
}

group = "com.albertattard.presentation"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

dependencies {
    val hikari = "3.4.2"
    val exposed = "0.17.7"
    val h2 = "1.4.200"

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    implementation("com.zaxxer:HikariCP:$hikari")
    implementation("org.jetbrains.exposed:exposed:$exposed")
    runtimeOnly("com.h2database:h2:$h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test")
}
