package shuttle.onboarding.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import studio.forface.shuttle.design.R

@Composable
fun OnboardingPage(actions: OnboardingPage.Actions) {
    OnboardingContent(actions = actions)
}

@Composable
private fun OnboardingContent(actions: OnboardingPage.Actions) {
    Column(
        modifier = Modifier
            .testTag(OnboardingPage.TEST_TAG)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(Dimens.Margin.XLarge),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.onboarding_main_title),
            style = MaterialTheme.typography.displayMedium
        )

        Image(
            modifier = Modifier.size(Dimens.Component.XXXLarge),
            painter = painterResource(id = R.drawable.ic_shuttle_foreground),
            contentDescription = stringResource(id = R.string.x_app_icon_description)
        )

        Text(
            text = stringResource(id = R.string.onboarding_main_description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = actions.onOnboardingComplete) {
                Text(text = stringResource(id = R.string.onboarding_action_complete))
            }
        }
    }
}

object OnboardingPage {

    const val TEST_TAG = "OnboardingPage"

    data class Actions(
        val onOnboardingComplete: () -> Unit
    )
}

@Composable
@Preview(showSystemUi = true)
private fun OnboardingContentPreview() {
    val actions = OnboardingPage.Actions(onOnboardingComplete = {})
    ShuttleTheme {
        OnboardingContent(actions = actions)
    }
}
