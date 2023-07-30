package shuttle.plugins.common

object KotlinDefaults {

    const val ContextReceiversCompilerArg = "-Xcontext-receivers"
    const val DelicateCoroutinesApi = "kotlinx.coroutines.DelicateCoroutinesApi"
    const val ExperimentalCoroutinesApi = "kotlinx.coroutines.ExperimentalCoroutinesApi"
    const val ExperimentalPagingApi = "androidx.paging.ExperimentalPagingApi"
    const val ExperimentalStdlibApi = "kotlin.ExperimentalStdlibApi"
    const val FlowPreview = "kotlinx.coroutines.FlowPreview"

    private val DefaultOptIns = listOf(
        "kotlin.experimental.ExperimentalTypeInference",
        "kotlin.time.ExperimentalTime"
    )

    private val TestOptIns = listOf(
        "shuttle.ShuttleTestApi"
    )

    val FreeCompilerArgs =
        DefaultOptIns.map { annotationName -> "-opt-in=$annotationName" } + ContextReceiversCompilerArg

    val TestFreeCompilerArgs = TestOptIns
        .map { annotationName -> "-opt-in=$annotationName" }
}
