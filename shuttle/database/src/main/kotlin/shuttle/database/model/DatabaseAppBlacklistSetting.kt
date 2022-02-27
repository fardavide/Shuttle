package shuttle.database.model

data class DatabaseAppBlacklistSetting(
    val appId: DatabaseAppId,
    val appName: String,
    val isBlacklisted: Boolean
)
