plugins {
    id("shuttle.detekt")
    id("shuttle.modulesCatalog")
    alias(libs.plugins.ksp)
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle.android)
        classpath(libs.gradle.crashlytics)
        classpath(libs.gradle.gms)
        classpath(libs.gradle.kotlin)
        classpath(libs.gradle.sqlDelight)
    }
}

apply(from = "gradle/applyProjectRepositories.gradle.kts")
