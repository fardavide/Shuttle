package shuttle.settings.domain

import arrow.core.Option
import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.model.AppBlacklistSetting
import shuttle.settings.domain.model.WidgetSettings

interface SettingsRepository {

    suspend fun isBlacklisted(appId: AppId): Boolean

    fun observeAppsBlacklistSettings(): Flow<List<AppBlacklistSetting>>

    fun observeCurrentIconPack(): Flow<Option<AppId>>

    fun observeWidgetSettings(): Flow<WidgetSettings>

    suspend fun setBlacklisted(appId: AppId, blacklisted: Boolean)

    suspend fun setCurrentIconPack(id: Option<AppId>)

    suspend fun updateWidgetSettings(settings: WidgetSettings)
}
