@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.io.File

fun Project.configureTestedExtension() {

    extensions.configure<TestedExtension> {

        compileSdkVersion(33)
        defaultConfig {
            minSdk = 26
            targetSdk = 33

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
            jvmTarget = JavaVersion.VERSION_11.toString()
        }

        signingConfigs {
            create("release") {

                storeFile(File("${rootDir.path}/keystore/keystore.jks"))
                storePassword(System.getenv("KEYSTORE_PASSWORD"))
                keyAlias(System.getenv("KEYSTORE_KEY_ALIAS"))
                keyPassword(System.getenv("KEYSTORE_KEY_PASSWORD"))
            }
        }

        buildTypes {
            named("release") {
                isMinifyEnabled = true
                signingConfig = signingConfigs["release"]
            }
        }

        if (this is LibraryExtension) libraryVariants.configureEach {
            sourceSets {
                getByName(this@configureEach.name) {
                    kotlin.srcDir("build/generated/ksp/${this.name}/kotlin")
                }
            }
        }
        if (this is AppExtension) applicationVariants.configureEach {
            sourceSets {
                getByName(this@configureEach.name) {
                    kotlin.srcDir("build/generated/ksp/${this.name}/kotlin")
                }
            }
        }

        packagingOptions.resources.excludes.addAll(
            listOf(
                "/META-INF/{AL2.0,LGPL2.1}"
            )
        )
    }
}
