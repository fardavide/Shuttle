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
            implementationClass = "ShuttleAndroidPlugin"
        }
    }
}

dependencies {

    implementation(project(":kotlin"))
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
}

