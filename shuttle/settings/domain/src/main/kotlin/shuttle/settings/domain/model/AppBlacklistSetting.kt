package shuttle.settings.domain.model

import shuttle.apps.domain.model.AppModel

data class AppBlacklistSetting(
    val app: AppModel,
    val inBlacklist: Boolean
)
