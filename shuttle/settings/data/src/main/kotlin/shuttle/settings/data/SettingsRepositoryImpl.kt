package shuttle.settings.data

import shuttle.apps.domain.model.AppId
import shuttle.database.datasource.SettingDataSource
import shuttle.database.model.DatabaseAppId
import shuttle.settings.domain.SettingsRepository

class SettingsRepositoryImpl(
    private val settingDataSource: SettingDataSource
) : SettingsRepository {

    override suspend fun isBlacklisted(appId: AppId) =
        settingDataSource.isBlacklisted(DatabaseAppId(appId.value))
}
