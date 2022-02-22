package shuttle.stats.domain.model

import shuttle.apps.domain.model.AppId

data class AppStats(
    val appId: AppId,
    val locationCounters: Collection<LocationCounter>,
    val timeCounters: Collection<TimeCounter>
)
