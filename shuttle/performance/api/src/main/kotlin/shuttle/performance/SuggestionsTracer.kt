package shuttle.performance

import org.koin.core.annotation.Factory
import shuttle.ShuttleTestApi

interface SuggestionsTracer {

    suspend fun <T> fromCache(block: suspend () -> T): T
    suspend fun <T> sort(block: suspend () -> T): T
}

@Factory
class RealSuggestionsTracer internal constructor(
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
