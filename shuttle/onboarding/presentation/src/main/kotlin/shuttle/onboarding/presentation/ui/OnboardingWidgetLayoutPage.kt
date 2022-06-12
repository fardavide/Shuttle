package shuttle.onboarding.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.NavigableListItem
import studio.forface.shuttle.design.R.drawable
import studio.forface.shuttle.design.R.string

@Composable
internal fun OnboardingWidgetLayoutPage(
    actions: OnboardingWidgetLayoutPage.Actions
) {
    OnboardingPageContent(
        index = OnboardingPage.Index.WIDGET_LAYOUT,
        title = string.onboarding_widget_layout_title,
        image = {
            LazyColumn(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.large
                )
            ) {
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
        previousButton = {
            Button(onClick = actions.onPreviousPage) {
                Text(text = stringResource(id = string.onboarding_action_previous))
            }
        },
        nextButton = {
            Button(onClick = actions.onNextPage) {
                Text(text = stringResource(id = string.onboarding_action_complete))
            }
        }
    )
}

internal object OnboardingWidgetLayoutPage {

    data class Actions(
        val onPreviousPage: () -> Unit,
        val onNextPage: () -> Unit
    )
}

@Composable
@Preview(showSystemUi = true)
private fun OnboardingWidgetLayoutPagePreview() {
    val actions = OnboardingWidgetLayoutPage.Actions(onPreviousPage = {}, onNextPage = {})
    ShuttleTheme {
        OnboardingWidgetLayoutPage(actions = actions)
    }
}
