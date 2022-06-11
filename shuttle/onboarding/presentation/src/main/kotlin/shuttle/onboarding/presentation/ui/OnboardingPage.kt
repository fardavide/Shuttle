package shuttle.onboarding.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun OnboardingPage(actions: OnboardingPage.Actions) {
    Box(modifier = Modifier.testTag(OnboardingPage.TEST_TAG)) {
        Button(onClick = actions.onOnboardingComplete) {
            Text(text = "complete")
        }
    }
}

object OnboardingPage {

    const val TEST_TAG = "OnboardingPage"

    data class Actions(
        val onOnboardingComplete: () -> Unit
    )
}
