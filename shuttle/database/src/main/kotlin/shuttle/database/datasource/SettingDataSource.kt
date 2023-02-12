package shuttle.database.datasource

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import shuttle.database.AppBlacklistSettingQueries
import shuttle.database.model.DatabaseAppBlacklistSetting
import shuttle.database.model.DatabaseAppId
import shuttle.database.util.suspendTransaction
import shuttle.database.util.suspendTransactionWithResult

interface SettingDataSource {

    fun findAllAppsWithBlacklistSetting(): Flow<List<DatabaseAppBlacklistSetting>>
    suspend fun isBlacklisted(appId: DatabaseAppId): Boolean

    suspend fun setBlacklisted(appId: DatabaseAppId, blacklisted: Boolean)
}

internal class SettingDataSourceImpl(
    private val appBlacklistSettingQueries: AppBlacklistSettingQueries,
    private val ioDispatcher: CoroutineDispatcher
) : SettingDataSource {

    override fun findAllAppsWithBlacklistSetting(): Flow<List<DatabaseAppBlacklistSetting>> =
        appBlacklistSettingQueries.findAllAppsWithBlacklistSetting { appId, appName, isBlacklisted ->
            DatabaseAppBlacklistSetting(appId, appName, isBlacklisted = isBlacklisted == 1L)
        }.asFlow().mapToList(ioDispatcher)


    override suspend fun isBlacklisted(appId: DatabaseAppId) =
        appBlacklistSettingQueries.suspendTransactionWithResult(ioDispatcher) {
            findAllAppsWithBlacklistSetting()
                .executeAsList()
                .find { it.appId == appId }
                ?.isBlacklisted == 1L
        }

    override suspend fun setBlacklisted(appId: DatabaseAppId, blacklisted: Boolean) {
        appBlacklistSettingQueries.suspendTransaction(ioDispatcher) {
            insertAppBlacklistSetting(appId, blacklisted)
        }
    }
}
