package shuttle.test.compose

import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText

fun hasText(
    @StringRes text: Int,
    substring: Boolean = false,
    ignoreCase: Boolean = false
): SemanticsMatcher = hasText(getString(text), substring, ignoreCase)

fun SemanticsNodeInteractionsProvider.onNodeWithText(
    @StringRes text: Int,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction =
    onNodeWithText(getString(text), substring, ignoreCase, useUnmergedTree = useUnmergedTree)

fun SemanticsNodeInteractionsProvider.onAllNodesWithText(
    @StringRes text: Int,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteractionCollection =
    onAllNodesWithText(getString(text), substring, ignoreCase, useUnmergedTree)
