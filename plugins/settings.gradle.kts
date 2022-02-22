rootProject.name = "Shuttle plugins"
apply(from = "../gradle/includeProjectsAndModules.gradle.kts")
apply(from = "../gradle/applyPluginsRepositories.gradle.kts")

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
