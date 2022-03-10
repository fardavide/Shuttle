plugins {
    id("shuttle.detekt")
    id("shuttle.modulesCatalog")
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle.android)
        classpath(libs.gradle.kotlin)
        classpath(libs.gradle.sqlDelight)
    }
}

apply(from = "gradle/applyProjectRepositories.gradle.kts")
