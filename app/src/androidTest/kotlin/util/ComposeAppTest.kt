@file:OptIn(ExperimentalTestApi::class)

package util

import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runAndroidComposeUiTest
import shuttle.app.MainActivity

@OptIn(ExperimentalTestApi::class)
fun runComposeAppTest(block: ComposeAppTest.() -> Unit) = runAndroidComposeUiTest(block = block)

typealias ComposeAppTest = AndroidComposeUiTest<MainActivity>
