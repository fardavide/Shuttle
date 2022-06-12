package shuttle.onboarding.presentation.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.NavigableListItem
import studio.forface.shuttle.design.R.drawable
import studio.forface.shuttle.design.R.string

@Composable
internal fun OnboardingWidgetLayoutPage(actions: OnboardingPage.NavigationActions) {
    OnboardingPageContent(
        index = OnboardingPage.Index.WIDGET_LAYOUT,
        title = string.onboarding_widget_layout_title,
        image = {
            LazyColumn(modifier = Modifier.imageContainerBackground()) {
                item {
                    NavigableListItem(
                        title = string.settings_widget_layout_grid,
                        icon = drawable.ic_grid,
                        onClick = {}
                    )
                }
                item {
                    NavigableListItem(
                        title = string.settings_widget_layout_icons_dimensions,
                        icon = drawable.ic_dimensions,
                        onClick = {}
                    )
                }
                item {
                    NavigableListItem(
                        title = string.settings_widget_layout_apps_labels,
                        icon = drawable.ic_label,
                        onClick = {}
                    )
                }
            }
        },
        description = string.onboarding_widget_layout_description,
        navigationActions = actions
    )
}

@Composable
@Preview(showSystemUi = true)
private fun OnboardingWidgetLayoutPagePreview() {
    val actions = OnboardingPage.NavigationActions(onPrevious = {}, onNext = {}, onComplete = {})
    ShuttleTheme {
        OnboardingWidgetLayoutPage(actions = actions)
    }
}
