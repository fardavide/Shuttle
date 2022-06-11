package shuttle.onboarding.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import studio.forface.shuttle.design.R
import studio.forface.shuttle.design.R.string

@Composable
internal fun OnboardingMainPage(actions: OnboardingMainPage.Actions) {
    OnboardingPageContent(
        title = string.onboarding_main_title,
        image = {
            Image(
                modifier = Modifier.size(Dimens.Component.XXXLarge),
                painter = painterResource(id = R.drawable.ic_shuttle_foreground),
                contentDescription = stringResource(id = string.x_app_icon_description)
            )
        },
        description = string.onboarding_main_description,
        nextButton = {
            Button(onClick = actions.onNextPage) {
                Text(text = stringResource(id = string.onboarding_action_next))
            }
        }
    )
}

internal object OnboardingMainPage {

    data class Actions(
        val onNextPage: () -> Unit
    )
}

@Composable
@Preview(showSystemUi = true)
private fun OnboardingMainPagePreview() {
    val actions = OnboardingMainPage.Actions(onNextPage = {})
    ShuttleTheme {
        OnboardingMainPage(actions = actions)
    }
}
