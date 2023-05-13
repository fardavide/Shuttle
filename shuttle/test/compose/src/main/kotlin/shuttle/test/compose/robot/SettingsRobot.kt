package shuttle.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import shuttle.design.TestTag

context(ComposeUiTest)
class SettingsRobot internal constructor() {

    fun verify(block: Verify.() -> Unit): SettingsRobot {
        block(Verify())
        return this
    }

    context(ComposeUiTest)
    class Verify internal constructor() {

        fun settingsIsDisplayed() {
            onNodeWithTag(TestTag.Settings).assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun SettingsRobot(content: @Composable () -> Unit): SettingsRobot {
    setContent(content)
    return SettingsRobot()
}
