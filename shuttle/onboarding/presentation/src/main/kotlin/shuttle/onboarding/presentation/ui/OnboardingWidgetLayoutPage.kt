package shuttle.onboarding.presentation.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.drawable.toDrawable
import shuttle.design.model.WidgetPreviewAppUiModel
import shuttle.design.model.WidgetPreviewUiModel
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.onboarding.presentation.model.OnboardingWidgetPreviewState
import shuttle.onboarding.presentation.viewmodel.OnboardingViewModel
import studio.forface.shuttle.design.R
import studio.forface.shuttle.design.R.string

@Composable
internal fun OnboardingWidgetLayoutPage(
    actions: OnboardingWidgetLayoutPage.Actions
) {
    OnboardingPageContent(
        index = OnboardingPage.Index.WIDGET_LAYOUT,
        title = string.onboarding_widget_layout_title,
        image = {
            Image(
                modifier = Modifier.size(Dimens.Component.XXXLarge),
                painter = painterResource(id = R.drawable.ic_shuttle_foreground),
                contentDescription = stringResource(id = string.x_app_icon_description)
            )
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
private fun OnboardingWidgetPagePreview() {
    val emptyDrawable = Bitmap
        .createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        .toDrawable(LocalContext.current.resources)

    val state = OnboardingWidgetPreviewState.Data(
        widgetPreview = WidgetPreviewUiModel(
            layout = OnboardingViewModel.WidgetLayout,
            apps = listOf(
                WidgetPreviewAppUiModel("Shuttle", emptyDrawable),
                WidgetPreviewAppUiModel("Proton Mail", emptyDrawable),
                WidgetPreviewAppUiModel("Proton Drive", emptyDrawable),
                WidgetPreviewAppUiModel("Telegram", emptyDrawable),
            )
        )
    )
    val actions = OnboardingWidgetPage.Actions(onPreviousPage = {}, onNextPage = {})
    ShuttleTheme {
        OnboardingWidgetPage(state = state, actions = actions)
    }
}
