@file:OptIn(ExperimentalTestApi::class)

package shuttle.app

import androidx.compose.ui.test.ExperimentalTestApi
import util.onboardingRobot
import util.runComposeAppTest
import kotlin.test.Test

class SettingsFlowTest {

    @Test
    fun test() = runComposeAppTest {
        onboardingRobot
            .skipOnboarding()
            .skipPermissions()
            .verify { settingsIsDisplayed() }
    }
}
