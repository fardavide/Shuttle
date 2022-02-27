package shuttle.settings.domain

import shuttle.apps.domain.model.AppId

interface SettingsRepository {

    suspend fun isBlacklisted(appId: AppId): Boolean
}
