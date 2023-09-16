package shuttle.performance.android

import com.google.firebase.perf.ktx.trace
import org.koin.core.annotation.Single
import shuttle.performance.Performance
import shuttle.performance.Trace
import com.google.firebase.perf.FirebasePerformance as GoogleFirebasePerformance

@Single
internal class FirebasePerformance(
    private val performance: GoogleFirebasePerformance
) : Performance {

    override suspend fun <T> trace(traceName: String, block: suspend (Trace) -> T): T =
        performance.newTrace(traceName).trace { block(FirebaseTraceWrapper(this)) }
}
