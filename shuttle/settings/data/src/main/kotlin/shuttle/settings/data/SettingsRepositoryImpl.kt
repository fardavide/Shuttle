package shuttle.settings.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.AppName
import shuttle.database.datasource.SettingDataSource
import shuttle.database.model.DatabaseAppId
import shuttle.settings.domain.SettingsRepository
import shuttle.settings.domain.model.AppBlacklistSetting

class SettingsRepositoryImpl(
    private val settingDataSource: SettingDataSource
) : SettingsRepository {

    override fun observeAppsBlacklistSettings(): Flow<List<AppBlacklistSetting>> =
        settingDataSource.findAllAppsWithBlacklistSetting().map { list ->
            list.map { databaseAppBlacklistSetting ->
                val app = AppModel(
                    id = AppId(databaseAppBlacklistSetting.appId.value),
                    name = AppName(databaseAppBlacklistSetting.appName)
                )
                AppBlacklistSetting(app, databaseAppBlacklistSetting.isBlacklisted)
            }
        }

    override suspend fun isBlacklisted(appId: AppId) =
        settingDataSource.isBlacklisted(DatabaseAppId(appId.value))

    override suspend fun setBlacklisted(appId: AppId, blacklisted: Boolean) {
        settingDataSource.setBlacklisted(DatabaseAppId(appId.value), blacklisted)
    }
}
