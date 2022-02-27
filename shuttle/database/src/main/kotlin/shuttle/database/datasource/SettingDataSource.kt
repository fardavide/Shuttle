package shuttle.database.datasource

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import shuttle.database.AppBlacklistSettingQueries
import shuttle.database.model.DatabaseAppId

interface SettingDataSource {

    suspend fun isBlacklisted(appId: DatabaseAppId): Boolean
}

internal class SettingDataSourceImpl(
    private val appBlacklistSettingQueries: AppBlacklistSettingQueries,
    private val ioDispatcher: CoroutineDispatcher
): SettingDataSource {

    override suspend fun isBlacklisted(appId: DatabaseAppId) = withContext(ioDispatcher) {
        appBlacklistSettingQueries.findAllAppsWithBlacklistSetting()
            .executeAsList()
            .find { it.appId == appId }
            ?.isBlacklisted == 1L
    }
}
