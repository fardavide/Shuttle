@file:OptIn(ExperimentalTestApi::class)

package shuttle.test.compose

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed

context(ComposeUiTest)
fun SemanticsNodeInteraction.awaitDisplayed(): SemanticsNodeInteraction {
    waitUntil { check { assertIsDisplayed() } }
    return this
}
