rootProject.name = "Shuttle"
apply(from = "gradle/includeProjectsAndModules.gradle.kts")
apply(from = "gradle/applyPluginsRepositories.gradle.kts")

enableFeaturePreview("VERSION_CATALOGS")
