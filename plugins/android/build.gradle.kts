plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin)
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        create("plugin") {
            id = "shuttle.android"
            displayName = "Shuttle Android"
            description = "Configure Android module"
            implementationClass = "shuttle.plugins.android.ShuttleAndroidPlugin"
        }
    }
}

dependencies {

    implementation(project(":kotlin"))
    implementation(libs.gradle.android)
    implementation(libs.gradle.kotlin)
}

