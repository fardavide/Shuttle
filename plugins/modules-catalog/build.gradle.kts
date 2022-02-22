plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin)
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("plugin") {
            id = "shuttle.modulesCatalog"
            displayName = "Shuttle Modules"
            description = "Add DSL for reference Project modules"
            implementationClass = "ShuttleModulesCatalogPlugin"
        }
    }
}

