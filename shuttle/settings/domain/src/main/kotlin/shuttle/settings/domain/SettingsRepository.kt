package shuttle.settings.domain

import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.model.AppBlacklistSetting
import shuttle.settings.domain.model.WidgetSettings

interface SettingsRepository {

    fun observeAppsBlacklistSettings(): Flow<List<AppBlacklistSetting>>

    fun observeWidgetSettings(): Flow<WidgetSettings>

    suspend fun isBlacklisted(appId: AppId): Boolean

    suspend fun setBlacklisted(appId: AppId, blacklisted: Boolean)

    suspend fun updateWidgetSettings(settings: WidgetSettings)
}
