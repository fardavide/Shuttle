@file:OptIn(ExperimentalTestApi::class)

package shuttle.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import shuttle.design.TestTag
import shuttle.test.compose.onNodeWithText
import studio.forface.shuttle.design.R.string

context(ComposeUiTest)
class PermissionsRobot internal constructor() {

    fun skipPermissions(): SettingsRobot {
        onNodeWithText(string.permissions_skip_permissions_action).performClick()
        return SettingsRobot()
    }

    fun verify(block: Verify.() -> Unit) {
        block(Verify())
    }

    context(ComposeUiTest)
    class Verify internal constructor() {

        fun permissionsIsDisplayed() {
            onNodeWithTag(TestTag.Permissions).assertIsDisplayed()
        }
    }
}

context(ComposeUiTest)
fun PermissionsRobot(content: @Composable () -> Unit): PermissionsRobot {
    setContent(content)
    return PermissionsRobot()
}
