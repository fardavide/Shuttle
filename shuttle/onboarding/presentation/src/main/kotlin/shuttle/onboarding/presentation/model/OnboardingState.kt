package shuttle.onboarding.presentation.model

import arrow.optics.optics
import shuttle.design.model.WidgetPreviewUiModel

@optics sealed interface OnboardingState {

    object Loading : OnboardingState

    @optics data class ShowOnboarding(
        val widgetPreview: OnboardingWidgetPreviewState
    ) : OnboardingState {
        companion object
    }

    object OnboardingAlreadyShown : OnboardingState

    companion object
}

@optics sealed interface OnboardingWidgetPreviewState {

    object Loading : OnboardingWidgetPreviewState

    @optics data class Data(val widgetPreview: WidgetPreviewUiModel): OnboardingWidgetPreviewState {
        companion object
    }

    companion object
}
