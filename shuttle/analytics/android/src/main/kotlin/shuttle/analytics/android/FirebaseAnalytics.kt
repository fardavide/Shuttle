package shuttle.analytics.android

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.koin.core.annotation.Single
import shuttle.analytics.Analytics
import shuttle.analytics.AnalyticsEvent

@Single
internal class FirebaseAnalytics : Analytics {

    init {
        Firebase.analytics.setAnalyticsCollectionEnabled(true)
    }

    override fun log(event: AnalyticsEvent) {
        Firebase.analytics.logEvent(event.name, event.toBundle())
    }
}

private fun AnalyticsEvent.toBundle() = Bundle().apply {
    putString("name", name)
    for ((k, v) in values) {
        when (v) {
            is String -> putString(k, v)
            is Int -> putInt(k, v)
            is Long -> putLong(k, v)
            is Float -> putFloat(k, v)
            is Double -> putDouble(k, v)
            is Boolean -> putBoolean(k, v)
            is Collection<*> -> putStringArrayList(k, ArrayList(v.map { it.toString() }))
            else -> error("Unsupported type ${v::class.java}")
        }
    }
}
