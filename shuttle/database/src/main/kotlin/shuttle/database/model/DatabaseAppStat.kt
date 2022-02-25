package shuttle.database.model

sealed interface DatabaseAppStat {
    val appId: DatabaseAppId
    val count: Int

    data class ByLocation(
        override val appId: DatabaseAppId,
        override val count: Int
    ): DatabaseAppStat

    data class ByTime(
        override val appId: DatabaseAppId,
        override val count: Int
    ): DatabaseAppStat
}
