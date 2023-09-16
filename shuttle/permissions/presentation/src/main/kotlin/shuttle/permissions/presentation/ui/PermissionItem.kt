package shuttle.permissions.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.PreviewUtils
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.permissions.presentation.model.PermissionItemUiModel
import shuttle.resources.R.string

@Composable
internal fun PermissionItem(permissionItem: PermissionItemUiModel, onRequestPermission: () -> Unit) {
    val containerColor =
        if (permissionItem.isGranted()) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.errorContainer
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier
            .padding(horizontal = Dimens.Margin.medium, vertical = Dimens.Margin.small)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = Dimens.Margin.medium, vertical = Dimens.Margin.small)
                .fillMaxWidth()
        ) {
            when (permissionItem) {
                is PermissionItemUiModel.Granted -> GrantedPermissionItem(permissionItem)
                is PermissionItemUiModel.NotGranted -> NotGrantedPermissionItem(permissionItem, onRequestPermission)
            }
        }
    }
}

@Composable
private fun GrantedPermissionItem(permissionItem: PermissionItemUiModel.Granted) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(permissionItem.name),
                style = MaterialTheme.typography.titleLarge
            )
            Icon(
                modifier = Modifier.size(Dimens.Icon.Medium),
                painter = rememberVectorPainter(image = Icons.Rounded.CheckCircle),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = stringResource(permissionItem.permissionGrantedDescription)
            )
        }
        Spacer(modifier = Modifier.height(Dimens.Margin.small))
        Text(
            text = stringResource(permissionItem.permissionGrantedDescription),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun NotGrantedPermissionItem(
    permissionItem: PermissionItemUiModel.NotGranted,
    onRequestPermission: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(permissionItem.name),
                style = MaterialTheme.typography.titleLarge
            )
            Icon(
                modifier = Modifier.size(Dimens.Icon.Medium),
                painter = rememberVectorPainter(image = Icons.Rounded.Warning),
                tint = MaterialTheme.colorScheme.errorContainer,
                contentDescription = stringResource(permissionItem.permissionNotGrantedDescription)
            )
        }
        Spacer(modifier = Modifier.height(Dimens.Margin.small))
        Text(
            text = stringResource(permissionItem.description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Dimens.Margin.small))
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onErrorContainer),
                onClick = onRequestPermission
            ) {
                Text(text = stringResource(permissionItem.buttonText))
            }
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = PreviewUtils.Dimens.Small.Width)
private fun GrantedPermissionItemPreview() {
    val uiModel = PermissionItemUiModel.Granted(
        name = string.permissions_location_background_name,
        permissionGrantedDescription = string.permissions_location_background_granted_description
    )
    ShuttleTheme {
        PermissionItem(uiModel, onRequestPermission = {})
    }
}

@Composable
@Preview(showBackground = true, widthDp = PreviewUtils.Dimens.Small.Width)
private fun NotGrantedPermissionItemPreview() {
    val uiModel = PermissionItemUiModel.NotGranted(
        name = string.permissions_location_background_name,
        description = string.permissions_location_background_description,
        permissionNotGrantedDescription = string.permissions_location_background_not_granted_description,
        buttonText = string.permissions_location_action
    )
    ShuttleTheme {
        PermissionItem(uiModel, onRequestPermission = {})
    }
}
