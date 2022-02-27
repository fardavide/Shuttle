package shuttle.settings.domain.usecase

import shuttle.apps.domain.model.AppId

class IsInBlacklist {

    suspend operator fun invoke(appId: AppId): Boolean =
        TODO("Not implemented")
}
