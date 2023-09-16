package shuttle.consents.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.Modal
import shuttle.resources.R.string

@Composable
fun ConsentsModal(actions: ConsentsModal.Actions) {
    Modal(onDismiss = actions.dismiss) {
        ConsentsModalContent(actions)
    }
}

@Composable
private fun ConsentsModalContent(actions: ConsentsModal.Actions) {
    val consentAndDismiss = {
        actions.consent()
        actions.dismiss()
    }
    val declineAndDismiss = {
        actions.decline()
        actions.dismiss()
    }

    Column(
        modifier = Modifier.padding(
            start = Dimens.Margin.large,
            end = Dimens.Margin.large,
            bottom = Dimens.Margin.large
        ),
        verticalArrangement = Arrangement.spacedBy(Dimens.Margin.large)
    ) {
        Text(
            text = stringResource(id = string.consents_data_title),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(id = string.consents_data_description)
        )
        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.medium)
        ) {
            OutlinedButton(onClick = declineAndDismiss) {
                Text(text = stringResource(id = string.action_decline))
            }
            FilledTonalButton(onClick = consentAndDismiss) {
                Text(text = stringResource(id = string.action_consent))
            }
        }
    }
}

object ConsentsModal {

    data class Actions(
        val consent: () -> Unit,
        val decline: () -> Unit,
        val dismiss: () -> Unit
    ) {
        companion object {
            val Empty = Actions(consent = {}, decline = {}, dismiss = {})
        }
    }
}

@Preview
@Composable
private fun ConsentsModalContentPreview() {
    ShuttleTheme { 
        ConsentsModalContent(actions = ConsentsModal.Actions.Empty)
    }
}
