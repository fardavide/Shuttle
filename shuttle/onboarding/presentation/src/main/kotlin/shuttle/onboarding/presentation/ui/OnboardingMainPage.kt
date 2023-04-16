package shuttle.onboarding.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.R
import shuttle.design.R.string
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme

@Composable
internal fun OnboardingMainPage(actions: OnboardingPage.NavigationActions) {
    OnboardingPageContent(
        index = OnboardingPage.Index.MAIN,
        title = string.onboarding_main_title,
        image = {
            Image(
                modifier = Modifier.size(Dimens.Component.XXXLarge),
                painter = painterResource(id = R.drawable.ic_shuttle_foreground),
                contentDescription = stringResource(id = string.x_app_icon_description)
            )
        },
        description = string.onboarding_main_description,
        navigationActions = actions
    )
}

@Composable
@Preview(showSystemUi = true)
private fun OnboardingMainPagePreview() {
    val actions = OnboardingPage.NavigationActions(onPrevious = {}, onNext = {}, onComplete = {})
    ShuttleTheme {
        OnboardingMainPage(actions = actions)
    }
}
