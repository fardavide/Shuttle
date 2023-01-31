@file:OptIn(ExperimentalTestApi::class)

package shuttle.test.compose

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runAndroidComposeUiTest

fun runComposeTest(block: ComposeTest.() -> Unit) = runAndroidComposeUiTest(block)

fun <T : ComponentActivity> AndroidComposeUiTest<T>.onBackPressed() {
    runOnUiThread { requireActivity().onBackPressed() }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.requireActivity() = requireNotNull(activity)

typealias ComposeTest = AndroidComposeUiTest<ComposeTestActivity>

class ComposeTestActivity : ComponentActivity()
