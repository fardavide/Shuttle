package shuttle.onboarding.presentation.model

import shuttle.design.model.WidgetPreviewUiModel

internal sealed interface OnboardingState {

    object Loading : OnboardingState

    data class Data(val widgetPreview: WidgetPreviewUiModel): OnboardingState
}
