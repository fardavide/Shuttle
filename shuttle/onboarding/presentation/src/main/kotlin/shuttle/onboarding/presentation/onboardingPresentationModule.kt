package shuttle.onboarding.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import shuttle.onboarding.presentation.mapper.OnboardingBlacklistUiModelMapper
import shuttle.onboarding.presentation.mapper.WidgetPreviewAppUiModelMapper
import shuttle.onboarding.presentation.viewmodel.OnboardingViewModel

val onboardingPresentationModule = module {

    factory { OnboardingBlacklistUiModelMapper(getIconDrawableForApp = get()) }
    factory { WidgetPreviewAppUiModelMapper(getIconDrawableForApp = get()) }

    viewModel {
        OnboardingViewModel(
            didShowOnboarding = get(),
            observeAllInstalledApps = get(),
            onboardingBlacklistUiModelMapper = get(),
            setOnboardingShown = get(),
            widgetPreviewAppUiModelMapper = get()
        )
    }
}
