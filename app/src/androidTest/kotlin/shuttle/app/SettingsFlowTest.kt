@file:OptIn(ExperimentalTestApi::class)

package shuttle.app

import androidx.compose.ui.test.ExperimentalTestApi
import util.runComposeAppTest
import util.settingsRobot
import kotlin.test.Test

class SettingsFlowTest {

    @Test
    fun settingsIsDisplayed() = runComposeAppTest {
        settingsRobot
            .verify { settingsIsDisplayed() }
    }
}
