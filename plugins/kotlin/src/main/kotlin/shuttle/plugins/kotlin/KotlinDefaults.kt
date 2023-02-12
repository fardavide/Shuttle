package shuttle.plugins.kotlin

object KotlinDefaults {

    val FreeCompilerArgs = listOf(
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-opt-in=kotlin.RequiresOptIn",
        "-Xcontext-receivers"
    )
}
