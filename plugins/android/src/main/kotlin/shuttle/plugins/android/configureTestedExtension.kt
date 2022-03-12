@file:Suppress("UnstableApiUsage")
package shuttle.plugins.android

import com.android.build.gradle.TestedExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

fun Project.configureTestedExtension() {

    extensions.configure<TestedExtension> {

        compileSdkVersion(32)
        defaultConfig {
            minSdk = 26
            targetSdk = 32

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

        packagingOptions.resources.excludes.addAll(
            listOf(
                "/META-INF/{AL2.0,LGPL2.1}"
            )
        )
    }
}
