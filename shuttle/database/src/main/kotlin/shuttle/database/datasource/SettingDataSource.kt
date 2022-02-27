package shuttle.database.datasource

import kotlinx.coroutines.CoroutineDispatcher
import shuttle.database.AppBlacklistSettingQueries
import shuttle.database.model.DatabaseAppId
import shuttle.database.util.suspendTransactionWithResult

interface SettingDataSource {

    suspend fun isBlacklisted(appId: DatabaseAppId): Boolean
}

internal class SettingDataSourceImpl(
    private val appBlacklistSettingQueries: AppBlacklistSettingQueries,
    private val ioDispatcher: CoroutineDispatcher
): SettingDataSource {

    override suspend fun isBlacklisted(appId: DatabaseAppId) =
        appBlacklistSettingQueries.suspendTransactionWithResult(ioDispatcher) {
            findAllAppsWithBlacklistSetting()
                .executeAsList()
                .find { it.appId == appId }
                ?.isBlacklisted == 1L
        }
}
