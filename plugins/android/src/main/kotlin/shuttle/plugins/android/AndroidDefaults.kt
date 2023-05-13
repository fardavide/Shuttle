package shuttle.plugins.android

import shuttle.plugins.common.KotlinDefaults

object AndroidDefaults {

    const val ExperimentalTestApi = "androidx.compose.ui.test.ExperimentalTestApi"
    const val ExperimentalMaterial3Api = "androidx.compose.material3.ExperimentalMaterial3Api"
    const val ExperimentalPermissionsApi = "com.google.accompanist.permissions.ExperimentalPermissionsApi"

    val FreeCompilerArgs = KotlinDefaults.FreeCompilerArgs
    val ComposeFreeCompilerArgs = FreeCompilerArgs + listOf(
        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
    )

    const val APPLICATION_ID = "studio.forface.shuttle"
    const val BUILD_TOOLS = "33.0.2"
    const val COMPILE_SDK = 33
    const val MIN_SDK = 26
    const val TARGET_SDK = 33
}
