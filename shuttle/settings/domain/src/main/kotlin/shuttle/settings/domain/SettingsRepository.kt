package shuttle.settings.domain

import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.model.AppBlacklistSetting

interface SettingsRepository {

    fun observeAppsBlacklistSettings(): Flow<List<AppBlacklistSetting>>

    suspend fun isBlacklisted(appId: AppId): Boolean

    suspend fun setBlacklisted(appId: AppId, blacklisted: Boolean)
}
