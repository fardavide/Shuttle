package shuttle.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.AppName
import shuttle.database.datasource.SettingDataSource
import shuttle.database.model.DatabaseAppId
import shuttle.settings.data.model.WidgetSettingsPreferenceKeys
import shuttle.settings.domain.SettingsRepository
import shuttle.settings.domain.model.AppBlacklistSetting
import shuttle.settings.domain.model.Dp
import shuttle.settings.domain.model.Sp
import shuttle.settings.domain.model.WidgetSettings

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
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

    override fun observeWidgetSettings(): Flow<WidgetSettings> =
        with(WidgetSettingsPreferenceKeys) {
            dataStore.data.map {
                WidgetSettings(
                    rowsCount = it[RowsCount] ?: WidgetSettings.Default.rowsCount,
                    columnsCount = it[ColumnsCount] ?: WidgetSettings.Default.columnsCount,
                    iconSize = it[IconSize]?.let(::Dp) ?: WidgetSettings.Default.iconSize,
                    spacing = it[Spacing]?.let(::Dp) ?: WidgetSettings.Default.spacing,
                    textSize = it[TextSize]?.let(::Sp) ?: WidgetSettings.Default.textSize
                )
            }
        }

    override suspend fun isBlacklisted(appId: AppId) =
        settingDataSource.isBlacklisted(DatabaseAppId(appId.value))

    override suspend fun setBlacklisted(appId: AppId, blacklisted: Boolean) {
        settingDataSource.setBlacklisted(DatabaseAppId(appId.value), blacklisted)
    }

    override suspend fun updateWidgetSettings(settings: WidgetSettings) {
        with(WidgetSettingsPreferenceKeys) {
            dataStore.edit {
                it[RowsCount] = settings.rowsCount
                it[ColumnsCount] = settings.columnsCount
                it[IconSize] = settings.iconSize.value
                it[Spacing] = settings.spacing.value
                it[TextSize] = settings.textSize.value
            }
        }
    }
}


