package shuttle.onboarding.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
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
internal fun OnboardingBlacklistPage(actions: OnboardingPage.NavigationActions) {
    OnboardingPageContent(
        index = OnboardingPage.Index.BLACKLIST,
        title = string.onboarding_blacklist_title,
        image = {
            Image(
                modifier = Modifier.size(Dimens.Component.XXXLarge),
                painter = painterResource(id = R.drawable.ic_shuttle_foreground),
                contentDescription = stringResource(id = string.x_app_icon_description)
            )
        },
        description = string.onboarding_blacklist_description,
        navigationActions = actions
    )
}

@Composable
@Preview(showSystemUi = true)
private fun OnboardingBlacklistPagePreview() {
    val actions = OnboardingPage.NavigationActions(onPrevious = {}, onNext = {}, onComplete = {})
    ShuttleTheme {
        OnboardingBlacklistPage(actions = actions)
    }
}
