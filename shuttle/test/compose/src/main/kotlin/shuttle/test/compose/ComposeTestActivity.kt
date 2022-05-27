package shuttle.test.compose

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule

fun createComposeTestRule(): ComposeContentTestRule =
    createAndroidComposeRule<ComposeTestActivity>()

internal class ComposeTestActivity : ComponentActivity()
