@file:OptIn(ExperimentalTestApi::class)

package shuttle.test.compose.robot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import shuttle.design.TestTag
import shuttle.resources.R.string
import shuttle.test.compose.onNodeWithText

context(ComposeUiTest)
class PermissionsRobot internal constructor() {

    fun skipPermissions(): SettingsRobot {
        onNodeWithText(string.permissions_skip_permissions_action)
            .performScrollTo()
            .performClick()
        return SettingsRobot()
    }

    fun verify(block: Verify.() -> Unit): PermissionsRobot {
        block(Verify())
        return this
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
