package shuttle.onboarding.presentation.mapper

import arrow.core.Either
import arrow.core.continuations.either
import shuttle.apps.domain.model.AppModel
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp
import shuttle.onboarding.presentation.model.OnboardingBlacklistAppUiModel
import shuttle.onboarding.presentation.model.OnboardingBlacklistUiModel
import kotlin.random.Random

class OnboardingBlacklistUiModelMapper(
    private val getIconDrawableForApp: GetSystemIconDrawableForApp
) {

    suspend fun toUiModel(app: AppModel): Either<GetSystemIconError, OnboardingBlacklistAppUiModel> =
        either {
            OnboardingBlacklistAppUiModel(
                name = app.name.value,
                icon = getIconDrawableForApp(id = app.id).bind(),
                isBlacklisted = Random.nextBoolean()
            )
        }

    suspend fun toUiModel(apps: Collection<AppModel>, take: Int): OnboardingBlacklistUiModel {
        val blacklistApps = apps
            .map { toUiModel(it) }
            .mapNotNull { it.orNull() }
            .shuffled()
            .take(take)
            .sortedBy { it.name }
            .sortedBy { it.isBlacklisted.not() }
        return OnboardingBlacklistUiModel(apps = blacklistApps)
    }
}
