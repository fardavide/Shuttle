package shuttle.settings.domain.usecase

import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.SettingsRepository
import shuttle.stats.domain.StatsRepository

class AddToBlacklist(
    private val settingsRepository: SettingsRepository,
    private val statRepository: StatsRepository
) {

    suspend operator fun invoke(appId: AppId) {
        settingsRepository.setBlacklisted(appId, blacklisted = true)
        statRepository.deleteCountersFor(appId)
    }
}
