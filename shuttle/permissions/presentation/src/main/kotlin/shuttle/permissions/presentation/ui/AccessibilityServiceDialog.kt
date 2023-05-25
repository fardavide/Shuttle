package shuttle.permissions.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.ShuttleModalBottomSheet
import shuttle.resources.R.string

@Composable
internal fun AccessibilityServiceDialog(
    actions: AccessibilityServiceDialog.Actions,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    ShuttleModalBottomSheet(sheetState = sheetState, onDismissRequest = actions.onDismiss) {
        Column(
            modifier = Modifier.padding(Dimens.Margin.Large),
            verticalArrangement = Arrangement.spacedBy(Dimens.Margin.Medium)
        ) {
            Text(text = stringResource(id = string.permissions_accessibility_dialog_disclosure))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = actions.onDismiss) {
                    Text(text = stringResource(id = string.permissions_accessibility_dialog_cancel_action))
                }
                TextButton(onClick = actions.onConfirm) {
                    Text(text = stringResource(id = string.permissions_accessibility_dialog_confirm_action))
                }
            }
        }
    }
}

object AccessibilityServiceDialog {

    data class Actions(
        val onConfirm: () -> Unit,
        val onDismiss: () -> Unit
    ) {
        companion object {
            val Empty = Actions(onConfirm = {}, onDismiss = {})
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun AccessibilityServiceDialogPreview() {
    ShuttleTheme {
        AccessibilityServiceDialog(
            AccessibilityServiceDialog.Actions.Empty,
            sheetState = rememberSheetState(initialValue = SheetValue.Expanded)
        )
    }
}

@Composable
private fun rememberSheetState(
    initialValue: SheetValue = SheetValue.Hidden,
    skipHiddenState: Boolean = false
): SheetState {
    val confirmValueChange: (SheetValue) -> Boolean = { true }
    val skipPartiallyExpanded = false
    return rememberSaveable(
        skipPartiallyExpanded, confirmValueChange,
        saver = SheetState.Saver(
            skipPartiallyExpanded = skipPartiallyExpanded,
            confirmValueChange = confirmValueChange
        )
    ) {
        SheetState(
            skipPartiallyExpanded = skipPartiallyExpanded,
            initialValue = initialValue,
            confirmValueChange = confirmValueChange,
            skipHiddenState = skipHiddenState
        )
    }
}
