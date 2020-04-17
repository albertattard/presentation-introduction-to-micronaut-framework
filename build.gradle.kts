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
