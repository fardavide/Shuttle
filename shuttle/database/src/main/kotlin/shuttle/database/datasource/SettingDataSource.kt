package shuttle.database.datasource

import kotlinx.coroutines.CoroutineDispatcher
import shuttle.database.AppBlacklistSettingQueries

interface SettingDataSource {


}

internal class SettingDataSourceImpl(
    private val appBlacklistSettingQueries: AppBlacklistSettingQueries,
    private val ioDispatcher: CoroutineDispatcher
): SettingDataSource {


}
