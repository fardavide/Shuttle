package shuttle.analytics

interface Analytics {

    fun log(event: AnalyticsEvent)
}
