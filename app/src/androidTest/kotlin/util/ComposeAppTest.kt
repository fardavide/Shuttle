@file:OptIn(ExperimentalTestApi::class)

package util

import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runAndroidComposeUiTest
import studio.forface.shuttle.MainActivity

fun runComposeAppTest(block: ComposeAppTest.() -> Unit) = runAndroidComposeUiTest(block)

typealias ComposeAppTest = AndroidComposeUiTest<MainActivity>
