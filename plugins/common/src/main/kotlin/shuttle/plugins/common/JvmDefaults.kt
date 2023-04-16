package shuttle.plugins.common

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object JvmDefaults {

    const val WARNINGS_AS_ERRORS = true
    const val JAVA_VERSION = 18
    val Target = JvmTarget.JVM_18
}
