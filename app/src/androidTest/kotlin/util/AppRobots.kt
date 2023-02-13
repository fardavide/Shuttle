@file:OptIn(ExperimentalTestApi::class)

package util

import androidx.compose.ui.test.ExperimentalTestApi
import shuttle.test.compose.robot.OnboardingRobot
import shuttle.test.compose.robot.PermissionsRobot

context(ComposeAppTest)
val onboardingRobot: OnboardingRobot
    get() = OnboardingRobot()

context(ComposeAppTest)
val permissionsRobot: PermissionsRobot
    get() = onboardingRobot
        .skipOnboarding()
