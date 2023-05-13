package shuttle.test.compose

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.runAndroidComposeUiTest

fun runComposeTest(block: ComposeTest.() -> Unit) = runAndroidComposeUiTest(block = block)

fun <T : ComponentActivity> AndroidComposeUiTest<T>.onBackPressed() {
    runOnUiThread { requireActivity().onBackPressedDispatcher.onBackPressed() }
}

fun <T : ComponentActivity> AndroidComposeUiTest<T>.requireActivity() = requireNotNull(activity)

typealias ComposeTest = AndroidComposeUiTest<ComposeTestActivity>

class ComposeTestActivity : ComponentActivity()
