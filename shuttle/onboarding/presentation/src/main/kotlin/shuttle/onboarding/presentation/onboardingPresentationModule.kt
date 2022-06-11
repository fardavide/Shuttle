package shuttle.onboarding.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import shuttle.onboarding.presentation.mapper.WidgetPreviewAppUiModelMapper
import shuttle.onboarding.presentation.viewmodel.OnboardingViewModel

val onboardingPresentationModule = module {

    factory { WidgetPreviewAppUiModelMapper(getIconDrawableForApp = get()) }

    viewModel {
        OnboardingViewModel(
            didShowOnboarding = get(),
            observeAllInstalledApps = get(),
            setOnboardingShown = get(),
            widgetPreviewAppUiModelMapper = get()
        )
    }
}
