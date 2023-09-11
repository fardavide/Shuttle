package shuttle.analytics

interface AnalyticsEvent {

    val name: String

    val values: Map<String, Any>
}

internal fun event(name: String, vararg values: Pair<String, Any>) = object : AnalyticsEvent {
    override val name = name
    override val values = values.toMap()
}
