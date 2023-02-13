@file:OptIn(ExperimentalTestApi::class)

package util

import androidx.compose.ui.test.ExperimentalTestApi
import shuttle.test.compose.robot.OnboardingRobot
import shuttle.test.compose.robot.PermissionsRobot
import shuttle.test.compose.robot.SettingsRobot

context(ComposeAppTest)
val onboardingRobot: OnboardingRobot
    get() = OnboardingRobot()

context(ComposeAppTest)
val permissionsRobot: PermissionsRobot
    get() = onboardingRobot
        .skipOnboarding()

context(ComposeAppTest)
val settingsRobot: SettingsRobot
    get() = permissionsRobot
        .skipPermissions()
