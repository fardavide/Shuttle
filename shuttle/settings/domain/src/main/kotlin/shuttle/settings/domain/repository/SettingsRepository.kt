package shuttle.settings.domain.repository

import arrow.core.Option
import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.model.AppBlacklistSetting
import shuttle.settings.domain.model.KeepStatisticsFor
import shuttle.settings.domain.model.WidgetSettings

interface SettingsRepository {

    suspend fun didShowOnboarding(): Boolean

    suspend fun hasEnabledAccessibilityService(): Boolean

    suspend fun isBlacklisted(appId: AppId): Boolean

    fun observeAppsBlacklistSettings(): Flow<List<AppBlacklistSetting>>

    fun observeCurrentIconPack(): Flow<Option<AppId>>

    fun observeDidShowConsents(): Flow<Boolean>

    fun observeIsDataCollectionEnabled(): Flow<Boolean>

    fun observeKeepStatisticsFor(): Flow<KeepStatisticsFor>

    fun observeUseExperimentalAppSorting(): Flow<Boolean>

    fun observeWidgetSettings(): Flow<WidgetSettings>

    suspend fun resetOnboardingShown()

    suspend fun setBlacklisted(appId: AppId, blacklisted: Boolean)

    suspend fun setCurrentIconPack(id: Option<AppId>)

    suspend fun setHasEnabledAccessibilityService()

    suspend fun setIsDataCollectionEnabled(isDataCollectionEnabled: Boolean)

    suspend fun setKeepStatisticsFor(keepStatisticsFor: KeepStatisticsFor)

    suspend fun setConsentsShown()

    suspend fun setOnboardingShow()

    suspend fun setUseExperimentalAppSorting(useExperimentalAppSorting: Boolean)

    suspend fun updateWidgetSettings(settings: WidgetSettings)
}
