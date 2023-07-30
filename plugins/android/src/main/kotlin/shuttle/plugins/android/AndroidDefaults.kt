package shuttle.plugins.android

import shuttle.plugins.common.KotlinDefaults

object AndroidDefaults {

    const val ExperimentalTestApi = "androidx.compose.ui.test.ExperimentalTestApi"
    const val ExperimentalPermissionsApi = "com.google.accompanist.permissions.ExperimentalPermissionsApi"
    private const val ExperimentalMaterial3Api = "androidx.compose.material3.ExperimentalMaterial3Api"

    val FreeCompilerArgs = KotlinDefaults.FreeCompilerArgs
    val ComposeFreeCompilerArgs = FreeCompilerArgs + listOf(
        "-opt-in=$ExperimentalMaterial3Api"
    )
    val TestFreeCompilerArgs = KotlinDefaults.TestFreeCompilerArgs

    const val APPLICATION_ID = "studio.forface.shuttle"
    const val BUILD_TOOLS = "34.0.0"
    const val COMPILE_SDK = 34
    const val MIN_SDK = 26
    const val TARGET_SDK = 34
}
