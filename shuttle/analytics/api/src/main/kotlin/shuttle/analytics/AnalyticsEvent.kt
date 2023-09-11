package shuttle.analytics

interface AnalyticsEvent {

    val name: String

    val values: Map<String, Any>
}

internal fun event(
    name: String,
    firstValue: Pair<String, Any>,
    vararg values: Pair<String, Any>
) = event(
    name = name,
    values = buildMap {
        put(firstValue.first, firstValue.second)
        for ((k, v) in values) {
            put(k, v)
        }
    }
)

internal inline fun event(name: String, values: AnalyticsEventBuilder.() -> Unit) = 
    event(name, AnalyticsEventBuilder().apply(values).map)

internal fun event(name: String, values: Map<String, Any>) = object : AnalyticsEvent {
    override val name = name
    override val values = values
}

internal class AnalyticsEventBuilder {
    
    private val mutableMap = mutableMapOf<String, Any>()
    val map get() = mutableMap.toMap()
 
    infix fun String.withValue(value: Any) {
        mutableMap[this] = value
    }
}
