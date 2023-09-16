package shuttle.settings.data.repository

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
import shuttle.settings.data.DataStoreProvider
import shuttle.settings.data.model.AppPreferenceKey
import shuttle.settings.data.model.WidgetPreferenceKey
import shuttle.settings.domain.model.AppBlacklistSetting
import shuttle.settings.domain.model.Dp
import shuttle.settings.domain.model.KeepStatisticsFor
import shuttle.settings.domain.model.Sp
import shuttle.settings.domain.model.WidgetSettings
import shuttle.settings.domain.repository.SettingsRepository

@Factory
internal class DataStoreSettingsRepository(
    dataStoreProvider: DataStoreProvider,
    private val settingDataSource: SettingDataSource
) : SettingsRepository {

    private val dataStore = dataStoreProvider.dataStore()

    override suspend fun didShowOnboarding(): Boolean = dataStore.data.map {
        it[AppPreferenceKey.DidShowOnboarding] ?: false
    }.first()

    override suspend fun hasEnabledAccessibilityService(): Boolean = dataStore.data.map {
        it[AppPreferenceKey.HasAccessibilityService] ?: false
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
        Option.fromNullable(it[AppPreferenceKey.CurrentIconPack]?.let(::AppId))
    }.distinctUntilChanged()

    override fun observeDidShowConsents(): Flow<Boolean> = dataStore.data.map {
        it[AppPreferenceKey.DidShowConsents] ?: false
    }.distinctUntilChanged()

    override fun observeIsDataCollectionEnabled(): Flow<Boolean> = dataStore.data.map {
        it[AppPreferenceKey.IsDataCollectionEnabled] ?: false
    }.distinctUntilChanged()

    override fun observeKeepStatisticsFor(): Flow<KeepStatisticsFor> = dataStore.data.map {
        it[AppPreferenceKey.KeepStatisticsFor]?.let(KeepStatisticsFor::fromMonths) ?: KeepStatisticsFor.Default
    }.distinctUntilChanged()

    override fun observeUseExperimentalAppSorting(): Flow<Boolean> = dataStore.data.map {
        it[AppPreferenceKey.UseExperimentalAppSorting] ?: false
    }.distinctUntilChanged()

    override fun observeWidgetSettings(): Flow<WidgetSettings> = with(WidgetPreferenceKey) {
        dataStore.data.map {
            WidgetSettings(
                allowTwoLines = it[AllowTwoLines] ?: WidgetSettings.Default.allowTwoLines,
                columnCount = it[ColumnsCount] ?: WidgetSettings.Default.columnCount,
                horizontalSpacing = it[HorizontalSpacing]?.let(::Dp) ?: WidgetSettings.Default.horizontalSpacing,
                iconsSize = it[IconSize]?.let(::Dp) ?: WidgetSettings.Default.iconsSize,
                rowCount = it[RowsCount] ?: WidgetSettings.Default.rowCount,
                textSize = it[TextSize]?.let(::Sp) ?: WidgetSettings.Default.textSize,
                transparency = it[Transparency] ?: WidgetSettings.Default.transparency,
                useMaterialColors = it[UseMaterialColors] ?: WidgetSettings.Default.useMaterialColors,
                verticalSpacing = it[VerticalSpacing]?.let(::Dp) ?: WidgetSettings.Default.verticalSpacing
            )
        }.distinctUntilChanged()
    }

    override suspend fun resetOnboardingShown() {
        dataStore.edit {
            it[AppPreferenceKey.DidShowOnboarding] = false
        }
    }

    override suspend fun setBlacklisted(appId: AppId, blacklisted: Boolean) {
        settingDataSource.setBlacklisted(DatabaseAppId(appId.value), blacklisted)
    }

    override suspend fun setCurrentIconPack(id: Option<AppId>) {
        dataStore.edit { preferences ->
            id.fold(
                ifEmpty = { preferences -= AppPreferenceKey.CurrentIconPack },
                ifSome = { preferences[AppPreferenceKey.CurrentIconPack] = it.value }
            )
        }
    }

    override suspend fun setHasEnabledAccessibilityService() {
        dataStore.edit {
            it[AppPreferenceKey.HasAccessibilityService] = true
        }
    }

    override suspend fun setIsDataCollectionEnabled(isDataCollectionEnabled: Boolean) {
        dataStore.edit {
            it[AppPreferenceKey.IsDataCollectionEnabled] = isDataCollectionEnabled
        }
    }

    override suspend fun setKeepStatisticsFor(keepStatisticsFor: KeepStatisticsFor) {
        dataStore.edit {
            it[AppPreferenceKey.KeepStatisticsFor] = keepStatisticsFor.toMonths()
        }
    }

    override suspend fun setConsentsShown() {
        dataStore.edit {
            it[AppPreferenceKey.DidShowConsents] = true
        }
    }

    override suspend fun setOnboardingShow() {
        dataStore.edit {
            it[AppPreferenceKey.DidShowOnboarding] = true
        }
    }

    override suspend fun setUseExperimentalAppSorting(useExperimentalAppSorting: Boolean) {
        dataStore.edit {
            it[AppPreferenceKey.UseExperimentalAppSorting] = useExperimentalAppSorting
        }
    }

    override suspend fun updateWidgetSettings(settings: WidgetSettings) {
        with(WidgetPreferenceKey) {
            dataStore.edit {
                it[AllowTwoLines] = settings.allowTwoLines
                it[ColumnsCount] = settings.columnCount
                it[HorizontalSpacing] = settings.horizontalSpacing.value
                it[IconSize] = settings.iconsSize.value
                it[RowsCount] = settings.rowCount
                it[TextSize] = settings.textSize.value
                it[Transparency] = settings.transparency
                it[UseMaterialColors] = settings.useMaterialColors
                it[VerticalSpacing] = settings.verticalSpacing.value
            }
        }
    }
}
