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

    companion object {

        const val StringLimit = 100
    }
}

private fun AnalyticsEvent.toBundle() = Bundle().apply {
    for ((k, v) in values) {
        when (v) {
            is String -> putAnalyticsString(k, v)
            is Number -> putAnalyticsNumber(k, v)
            is Boolean -> putAnalyticsBoolean(k, v)
            is Collection<*> -> putAnalyticsCollection(k, v)
            else -> error("Unsupported type ${v::class.java}")
        }
    }
}

private fun Bundle.putAnalyticsBoolean(key: String, value: Boolean): Bundle =
    apply { putString(key, value.toString()) }

private fun Bundle.putAnalyticsNumber(key: String, value: Number): Bundle = apply { 
    when (value) {
        is Int -> putLong(key, value.toLong())
        is Long -> putLong(key, value)
        is Float -> putDouble(key, value.toDouble())
        is Double -> putDouble(key, value)
    }
}

private fun Bundle.putAnalyticsString(key: String, value: String): Bundle = apply {
    if (value.length <= FirebaseAnalytics.StringLimit) {
        putString(key, value)
    } else {
        value.chunked(FirebaseAnalytics.StringLimit).forEachIndexed { index, chunk ->
            putString("$key$index", chunk)
        }
    }
}

@Suppress("NestedBlockDepth")
private fun Bundle.putAnalyticsCollection(key: String, value: Collection<*>): Bundle = apply {
    val listAsString = value.joinToString()
    if (listAsString.length <= FirebaseAnalytics.StringLimit) {
        putString(key, listAsString)

    } else {
        var string = ""
        var eventValueIndex = 0

        for ((index, item) in value.withIndex()) {
            val isLast = index == value.size - 1

            val itemString = item.toString()
            if (string.length + itemString.length < FirebaseAnalytics.StringLimit) {
                string += itemString
                if (isLast.not()) {
                    string += ","
                }

            } else {
                putString("$key${eventValueIndex++}", string)
                string = itemString
                if (isLast.not()) {
                    string += ","
                }
            }
        }

        if (string.isNotEmpty()) {
            putString("$key$eventValueIndex", string)
        }
    }
}
