package shuttle.performance

import org.koin.core.annotation.Factory
import shuttle.ShuttleTestApi

/**
 * Tracer for Location performance.
 */
interface LocationTracer {

    /**
     * Fresh location is requested
     */
    suspend fun <T> fresh(block: suspend () -> T): T

    /**
     * Last location is requested
     */
    suspend fun <T> last(block: suspend () -> T): T
}

@Factory
internal class RealLocationTracer internal constructor(
    private val performance: Performance
) : LocationTracer {

    override suspend fun <T> fresh(block: suspend () -> T): T = performance.trace("$Name-fresh") { block() }

    override suspend fun <T> last(block: suspend () -> T): T = performance.trace("$Name-last") { block() }

    companion object {

        const val Name = "Location"
    }
}

@ShuttleTestApi
class FakeLocationTracer : LocationTracer {

    override suspend fun <T> fresh(block: suspend () -> T): T = block()

    override suspend fun <T> last(block: suspend () -> T): T = block()
}
