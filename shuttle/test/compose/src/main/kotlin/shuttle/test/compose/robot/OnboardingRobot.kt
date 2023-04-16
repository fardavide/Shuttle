@file:OptIn(ExperimentalTestApi::class)

package shuttle.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import shuttle.design.R.string
import shuttle.design.TestTag
import shuttle.test.compose.checkIsDisplayed
import shuttle.test.compose.hasText

context(ComposeUiTest)
class OnboardingRobot {

    fun skipOnboarding(): PermissionsRobot {
        while (onNodeWithTag(TestTag.Onboarding).checkIsDisplayed()) {
            onNode(hasText(string.onboarding_action_next) or hasText(string.onboarding_action_complete)).performClick()
        }
        return PermissionsRobot()
    }

    fun verify(block: Verify.() -> Unit): OnboardingRobot {
        block(Verify())
        return this
    }

    context(ComposeUiTest)
    class Verify internal constructor() {

        fun onboardingIsDisplayed() {
            onNodeWithTag(TestTag.Onboarding).assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun OnboardingRobot(content: @Composable () -> Unit): OnboardingRobot {
    setContent(content)
    return OnboardingRobot()
}
