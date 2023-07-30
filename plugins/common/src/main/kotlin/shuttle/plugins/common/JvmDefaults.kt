package shuttle.plugins.common

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object JvmDefaults {

    // TODO: Set to true. Cannot suppress the following warning:
    //  w: Language version 2.0 is experimental, there are no backwards compatibility guarantees for new
    //  language and library features
    const val WARNINGS_AS_ERRORS = false
    const val JAVA_VERSION = 18
    val Target = JvmTarget.JVM_18
}
