package shuttle.settings.data

import androidx.datastore.preferences.core.edit
import arrow.core.Option
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.AppName
import shuttle.database.datasource.SettingDataSource
import shuttle.database.model.DatabaseAppId
import shuttle.settings.data.model.CurrentIconPackPreferenceKey
import shuttle.settings.data.model.DidShowOnboardingPreferenceKey
import shuttle.settings.data.model.HasAccessibilityServicePreferenceKey
import shuttle.settings.data.model.WidgetSettingsPreferenceKeys
import shuttle.settings.domain.SettingsRepository
import shuttle.settings.domain.model.AppBlacklistSetting
import shuttle.settings.domain.model.Dp
import shuttle.settings.domain.model.Sp
import shuttle.settings.domain.model.WidgetSettings

@Factory
internal class SettingsRepositoryImpl(
    dataStoreProvider: DataStoreProvider,
    private val settingDataSource: SettingDataSource
) : SettingsRepository {

    private val dataStore = dataStoreProvider.dataStore()

    override suspend fun didShowOnboarding(): Boolean = dataStore.data.map {
        it[DidShowOnboardingPreferenceKey] ?: false
    }.first()

    override suspend fun hasEnabledAccessibilityService(): Boolean = dataStore.data.map {
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

    override fun observeCurrentIconPack(): Flow<Option<AppId>> = dataStore.data.map {
        Option.fromNullable(it[CurrentIconPackPreferenceKey]?.let(::AppId))
    }.distinctUntilChanged()

    override fun observeWidgetSettings(): Flow<WidgetSettings> = with(WidgetSettingsPreferenceKeys) {
        dataStore.data.map {
            WidgetSettings(
                allowTwoLines = it[AllowTwoLines] ?: WidgetSettings.Default.allowTwoLines,
                columnsCount = it[ColumnsCount] ?: WidgetSettings.Default.columnsCount,
                horizontalSpacing = it[HorizontalSpacing]?.let(::Dp) ?: WidgetSettings.Default.horizontalSpacing,
                iconsSize = it[IconSize]?.let(::Dp) ?: WidgetSettings.Default.iconsSize,
                rowsCount = it[RowsCount] ?: WidgetSettings.Default.rowsCount,
                textSize = it[TextSize]?.let(::Sp) ?: WidgetSettings.Default.textSize,
                transparency = it[Transparency] ?: WidgetSettings.Default.transparency,
                useMaterialColors = it[UseMaterialColors] ?: WidgetSettings.Default.useMaterialColors,
                verticalSpacing = it[VerticalSpacing]?.let(::Dp) ?: WidgetSettings.Default.verticalSpacing
            )
        }.distinctUntilChanged()
    }

    override suspend fun resetOnboardingShown() {
        dataStore.edit {
            it[DidShowOnboardingPreferenceKey] = false
        }
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

    override suspend fun setOnboardingShow() {
        dataStore.edit {
            it[DidShowOnboardingPreferenceKey] = true
        }
    }

    override suspend fun updateWidgetSettings(settings: WidgetSettings) {
        with(WidgetSettingsPreferenceKeys) {
            dataStore.edit {
                it[AllowTwoLines] = settings.allowTwoLines
                it[ColumnsCount] = settings.columnsCount
                it[HorizontalSpacing] = settings.horizontalSpacing.value
                it[IconSize] = settings.iconsSize.value
                it[RowsCount] = settings.rowsCount
                it[TextSize] = settings.textSize.value
                it[Transparency] = settings.transparency
                it[UseMaterialColors] = settings.useMaterialColors
                it[VerticalSpacing] = settings.verticalSpacing.value
            }
        }
    }
}
