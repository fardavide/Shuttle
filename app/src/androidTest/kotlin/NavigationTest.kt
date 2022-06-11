import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import shuttle.onboarding.presentation.ui.OnboardingPage
import shuttle.permissions.presentation.ui.PermissionsPage
import shuttle.test.compose.onBackPressed
import shuttle.test.compose.onNodeWithText
import shuttle.test.compose.requireActivity
import studio.forface.shuttle.design.R
import util.ComposeAppTest
import util.runComposeAppTest
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class NavigationTest {

    @Test
    fun whenStart_thenOnboardingIsShown() = runComposeAppTest {

        onNodeWithTag(OnboardingPage.TEST_TAG)
            .assertIsDisplayed()
    }

    @Test
    fun whenOnboardingIsClosed_thenPermissionsIsShown() = runComposeAppTest {

        // when
        completeOnboarding()

        // then
        onNodeWithTag(PermissionsPage.TEST_TAG)
            .assertIsDisplayed()
    }

    @Test
    fun givenOnboardingIsCompleted_whenBackIsPressed_thenAppIsClosed() = runComposeAppTest {

        // given
        completeOnboarding()

        // when
        onBackPressed()

        // then
        assertTrue(requireActivity().isFinishing)
    }

    private fun ComposeAppTest.completeOnboarding() {
        onNodeWithText(R.string.onboarding_action_complete)
            .performClick()
    }
}
