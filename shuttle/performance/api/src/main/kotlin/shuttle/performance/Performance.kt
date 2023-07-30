package shuttle.performance

import shuttle.ShuttleTestApi
import shuttle.notImplementedFake

interface Performance {

    suspend fun <T> trace(traceName: String, block: suspend (Trace) -> T): T
}

@ShuttleTestApi
class FakePerformance : Performance {

    override suspend fun <T> trace(traceName: String, block: suspend (Trace) -> T): T = notImplementedFake()
}
