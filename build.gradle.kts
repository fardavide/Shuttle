plugins {
    id("shuttle.modulesCatalog")
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.gradle)
        classpath(libs.kotlin.gradle)
        classpath(libs.sqlDelight.gradle)
    }
}

apply(from = "gradle/applyProjectRepositories.gradle.kts")
