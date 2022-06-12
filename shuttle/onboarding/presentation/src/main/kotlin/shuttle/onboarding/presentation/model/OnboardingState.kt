package shuttle.onboarding.presentation.model

import shuttle.design.model.WidgetPreviewUiModel

sealed interface OnboardingState {

    object Loading : OnboardingState

    data class ShowOnboarding(
        val widgetPreview: OnboardingWidgetPreviewState,
        val blacklist: OnboardingBlacklistState
    ) : OnboardingState {

        companion object {

            val Loading = ShowOnboarding(
                widgetPreview = OnboardingWidgetPreviewState.Loading,
                blacklist = OnboardingBlacklistState.Loading
            )
        }
    }

    object OnboardingAlreadyShown : OnboardingState
}

sealed interface OnboardingWidgetPreviewState {

    object Loading : OnboardingWidgetPreviewState

    data class Data(val widgetPreview: WidgetPreviewUiModel): OnboardingWidgetPreviewState
}

sealed interface OnboardingBlacklistState {

    object Loading : OnboardingBlacklistState

    data class Data(val blacklist: OnboardingBlacklistUiModel) : OnboardingBlacklistState
}
