package shuttle.performance

import org.koin.core.annotation.Factory
import shuttle.ShuttleTestApi

/**
 * Tracer for Suggestions performance.
 */
interface SuggestionsTracer {

    /**
     * Loading suggestions from cache (legacy method)
     */
    suspend fun <T> fromCache(block: suspend () -> T): T

    /**
     * Sorting suggestions (legacy method)
     */
    suspend fun <T> sort(block: suspend () -> T): T
}

@Factory
internal class RealSuggestionsTracer internal constructor(
    private val performance: Performance
) : SuggestionsTracer {

    override suspend fun <T> fromCache(block: suspend () -> T): T =
        performance.trace("$Name-fromCache") { block() }
    override suspend fun <T> sort(block: suspend () -> T): T = performance.trace("$Name-sort") { block() }

    companion object {

        const val Name = "Suggestions"
    }
}

@ShuttleTestApi
class FakeSuggestionsTracer : SuggestionsTracer {

    override suspend fun <T> fromCache(block: suspend () -> T): T = block()

    override suspend fun <T> sort(block: suspend () -> T): T = block()
}
