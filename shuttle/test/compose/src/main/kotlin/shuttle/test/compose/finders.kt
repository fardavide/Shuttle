package shuttle.test.compose

import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasText

fun SemanticsNodeInteractionsProvider.onNodeWithText(
    @StringRes text: Int,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction = onNode(hasText(getString(text), substring, ignoreCase), useUnmergedTree)
