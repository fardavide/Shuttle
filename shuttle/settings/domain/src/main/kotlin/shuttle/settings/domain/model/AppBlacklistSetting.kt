package shuttle.settings.domain.model

import shuttle.apps.domain.model.AppId

data class AppBlacklistSetting(
    val appId: AppId,
    val inBlacklist: Boolean
)
