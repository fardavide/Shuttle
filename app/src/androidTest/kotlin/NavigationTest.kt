import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import shuttle.design.TestTag
import shuttle.permissions.presentation.ui.PermissionsPage
import shuttle.test.compose.hasText
import shuttle.test.compose.onBackPressed
import shuttle.test.compose.requireActivity
import studio.forface.shuttle.design.R.string
import util.ComposeAppTest
import util.runComposeAppTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class NavigationTest {

    @Test
    @Ignore("Onboarding won't be shown more than one, we need to mock this behaviour")
    fun whenStart_thenOnboardingIsShown() = runComposeAppTest {

        onNodeWithTag(TestTag.Onboarding)
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
        while (true) {
            try {
                onNode(hasText(string.onboarding_action_next) or hasText(string.onboarding_action_complete))
                    .performClick()
            } catch (ignored: AssertionError) {
                return
            }
        }
    }
}
