package shuttle.onboarding.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import shuttle.apps.domain.sample.AppNameSample
import shuttle.design.model.WidgetPreviewAppUiModel
import shuttle.design.model.WidgetPreviewUiModel
import shuttle.design.sample.DrawableSample
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.LoadingSpinner
import shuttle.design.ui.WidgetPreview
import shuttle.onboarding.presentation.model.OnboardingWidgetPreviewState
import shuttle.onboarding.presentation.viewmodel.OnboardingViewModel
import shuttle.resources.R.string

@Composable
internal fun OnboardingWidgetPage(state: OnboardingWidgetPreviewState, actions: OnboardingPage.NavigationActions) {
    OnboardingPageContent(
        index = OnboardingPage.Index.WIDGET,
        title = string.onboarding_widget_title,
        content = {
            when (state) {
                OnboardingWidgetPreviewState.Loading -> LoadingSpinner()
                is OnboardingWidgetPreviewState.Data -> WidgetPreview(
                    model = state.widgetPreview
                )
            }
        },
        description = string.onboarding_widget_description,
        navigationActions = actions
    )
}

@Composable
@Preview(showSystemUi = true)
private fun OnboardingWidgetPagePreview() {
    val state = OnboardingWidgetPreviewState.Data(
        widgetPreview = WidgetPreviewUiModel(
            layout = OnboardingViewModel.WidgetLayout,
            apps = listOf(
                WidgetPreviewAppUiModel(AppNameSample.Shuttle.value, DrawableSample.Empty),
                WidgetPreviewAppUiModel(AppNameSample.CineScout.value, DrawableSample.Empty),
                WidgetPreviewAppUiModel(AppNameSample.MovieBase.value, DrawableSample.Empty),
                WidgetPreviewAppUiModel(AppNameSample.Slack.value, DrawableSample.Empty)
            )
        )
    )
    val actions = OnboardingPage.NavigationActions(onPrevious = {}, onNext = {}, onComplete = {})
    ShuttleTheme {
        OnboardingWidgetPage(state = state, actions = actions)
    }
}
