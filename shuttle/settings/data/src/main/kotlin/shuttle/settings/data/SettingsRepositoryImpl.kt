package shuttle.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import arrow.core.Option
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.AppName
import shuttle.database.datasource.SettingDataSource
import shuttle.database.model.DatabaseAppId
import shuttle.settings.data.model.CurrentIconPackPreferenceKey
import shuttle.settings.data.model.HasAccessibilityServicePreferenceKey
import shuttle.settings.data.model.PrioritizeLocationPreferenceKey
import shuttle.settings.data.model.WidgetSettingsPreferenceKeys
import shuttle.settings.domain.SettingsRepository
import shuttle.settings.domain.model.AppBlacklistSetting
import shuttle.settings.domain.model.Dp
import shuttle.settings.domain.model.Sp
import shuttle.settings.domain.model.WidgetSettings

internal class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    migratePreferences: MigratePreferences,
    private val settingDataSource: SettingDataSource
) : SettingsRepository {

    init {
        migratePreferences()
    }

    override suspend fun hasEnabledAccessibilityService(): Boolean =
        dataStore.data.map {
            it[HasAccessibilityServicePreferenceKey] ?: false
        }.first()

    override suspend fun isBlacklisted(appId: AppId) =
        settingDataSource.isBlacklisted(DatabaseAppId(appId.value))

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

    override fun observeCurrentIconPack(): Flow<Option<AppId>> =
        dataStore.data.map {
            Option.fromNullable(it[CurrentIconPackPreferenceKey]?.let(::AppId))
        }.distinctUntilChanged()

    override fun observePrioritizeLocation(): Flow<Boolean> =
        dataStore.data.map {
            it[PrioritizeLocationPreferenceKey] ?: false
        }

    override fun observeWidgetSettings(): Flow<WidgetSettings> =
        with(WidgetSettingsPreferenceKeys) {
            dataStore.data.map {
                WidgetSettings(
                    rowsCount = it[RowsCount] ?: WidgetSettings.Default.rowsCount,
                    columnsCount = it[ColumnsCount] ?: WidgetSettings.Default.columnsCount,
                    iconsSize = it[IconSize]?.let(::Dp) ?: WidgetSettings.Default.iconsSize,
                    horizontalSpacing = it[HorizontalSpacing]?.let(::Dp) ?: WidgetSettings.Default.horizontalSpacing,
                    verticalSpacing = it[VerticalSpacing]?.let(::Dp) ?: WidgetSettings.Default.verticalSpacing,
                    textSize = it[TextSize]?.let(::Sp) ?: WidgetSettings.Default.textSize
                )
            }.distinctUntilChanged()
        }

    override suspend fun setBlacklisted(appId: AppId, blacklisted: Boolean) {
        settingDataSource.setBlacklisted(DatabaseAppId(appId.value), blacklisted)
    }

    override suspend fun setCurrentIconPack(id: Option<AppId>) {
        dataStore.edit { preferences ->
            id.fold(
                ifEmpty = { preferences -= CurrentIconPackPreferenceKey },
                ifSome = { preferences[CurrentIconPackPreferenceKey] = it.value }
            )
        }
    }

    override suspend fun setHasEnabledAccessibilityService() {
        dataStore.edit {
            it[HasAccessibilityServicePreferenceKey] = true
        }
    }

    override suspend fun updatePrioritizeLocation(prioritizeLocation: Boolean) {
        dataStore.edit {
            it[PrioritizeLocationPreferenceKey] = prioritizeLocation
        }
    }

    override suspend fun updateWidgetSettings(settings: WidgetSettings) {
        with(WidgetSettingsPreferenceKeys) {
            dataStore.edit {
                it[RowsCount] = settings.rowsCount
                it[ColumnsCount] = settings.columnsCount
                it[IconSize] = settings.iconsSize.value
                it[HorizontalSpacing] = settings.horizontalSpacing.value
                it[VerticalSpacing] = settings.verticalSpacing.value
                it[TextSize] = settings.textSize.value
            }
        }
    }
}


