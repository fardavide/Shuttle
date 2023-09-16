package shuttle.permissions.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.Modal
import shuttle.resources.R.string

@Composable
internal fun AccessibilityServiceDialog(actions: AccessibilityServiceDialog.Actions) {
    Modal(onDismiss = actions.onDismiss) {
        Column(
            modifier = Modifier.padding(Dimens.Margin.large),
            verticalArrangement = Arrangement.spacedBy(Dimens.Margin.medium)
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
            AccessibilityServiceDialog.Actions.Empty
        )
    }
}
