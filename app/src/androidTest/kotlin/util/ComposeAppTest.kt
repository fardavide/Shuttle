@file:OptIn(ExperimentalTestApi::class)

package util

import androidx.compose.ui.test.AndroidComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runAndroidComposeUiTest
import shuttle.app.MainActivity
import shuttle.test.compose.robot.OnboardingRobot

fun runComposeAppTest(block: ComposeAppTest.() -> Unit) = runAndroidComposeUiTest(block = block)

typealias ComposeAppTest = AndroidComposeUiTest<MainActivity>

context(ComposeAppTest)
val onboardingRobot get() = OnboardingRobot()
