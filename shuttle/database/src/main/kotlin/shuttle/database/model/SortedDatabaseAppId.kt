package shuttle.database.model

data class SortedDatabaseAppId(
    val appId: DatabaseAppId,
    val order: Int
)
