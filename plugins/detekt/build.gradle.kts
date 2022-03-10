plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin)
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("plugin") {
            id = "shuttle.detekt"
            displayName = "Shuttle Detekt"
            description = "Configure Detekt"
            implementationClass = "ShuttleDetektPlugin"
        }
    }
}

dependencies {

    implementation(libs.gradle.detekt)
}

