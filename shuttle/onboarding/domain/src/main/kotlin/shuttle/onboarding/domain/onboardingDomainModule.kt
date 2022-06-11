package shuttle.onboarding.domain

import org.koin.dsl.module
import shuttle.onboarding.domain.usecase.DidShowOnboarding
import shuttle.onboarding.domain.usecase.SetOnboardingShown

val onboardingDomainModule = module {

    factory { DidShowOnboarding(settingsRepository = get()) }
    factory { SetOnboardingShown(settingsRepository = get()) }
}
