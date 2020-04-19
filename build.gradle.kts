plugins {
    base
    kotlin("jvm") version "1.3.71" apply false
}

allprojects {
    repositories {
        mavenLocal()
        jcenter()
    }
}

subprojects {
    version = "1.0"
}

defaultTasks("clean", "ktlintFormat", "dependencyUpdates", "test")
