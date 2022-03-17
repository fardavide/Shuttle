package shuttle.permissions.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.permissions.resources.Strings
import shuttle.permissions.resources.get

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AccessibilityServiceDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        ElevatedCard {
            Column(modifier = Modifier.padding(Dimens.Margin.Medium)) {
                Text(text = Strings.Accessibility.Dialog::Disclosure.get())
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) {
                        Text(text = Strings.Accessibility.Dialog::CancelAction.get())
                    }
                    TextButton(onClick = onConfirm) {
                        Text(text = Strings.Accessibility.Dialog::ConfirmAction.get())
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true) 
private fun AccessibilityServiceDialogPreview() {
    ShuttleTheme {
        AccessibilityServiceDialog(onConfirm = {}, onDismiss = {})
    }
}
