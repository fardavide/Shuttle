package shuttle.performance.android

import shuttle.performance.Trace
import com.google.firebase.perf.metrics.Trace as FirebaseTrace

@JvmInline
internal value class FirebaseTraceWrapper(private val firebaseTrace: FirebaseTrace) : Trace {

    override fun set(name: String, value: String) {
        firebaseTrace.putAttribute(name, value)
    }
}
