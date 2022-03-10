plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin)
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("plugin") {
            id = "shuttle.kotlin"
            displayName = "Shuttle Kotlin"
            description = "Configure Kotlin module"
            implementationClass = "ShuttleKotlinPlugin"
        }
    }
}

dependencies {

    implementation(libs.gradle.kotlin)
}

