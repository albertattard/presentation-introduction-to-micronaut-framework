import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.3.71" apply false
}

allprojects {
    repositories {
        mavenLocal()
        jcenter()
    }

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
            kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
            kotlinOptions.jvmTarget = "11"
            kotlinOptions.javaParameters = true
        }
    }
}

subprojects {
    version = "1.0"
}

defaultTasks("clean", "ktlintFormat", "dependencyUpdates", "build")
