package shuttle.test.compose

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed

inline fun check(block: () -> SemanticsNodeInteraction): Boolean {
    return try {
        block()
        true
    } catch (e: AssertionError) {
        false
    }
}

fun SemanticsNodeInteraction.checkIsDisplayed() = check { assertIsDisplayed() }
