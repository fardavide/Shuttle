package shuttle.permissions.ui

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.PreviewDimens
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.permissions.model.PermissionItemUiModel
import shuttle.permissions.resources.Strings
import shuttle.permissions.resources.get

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun PermissionItem(
    permissionItem: PermissionItemUiModel,
    onRequestPermission: () -> Unit
) {
    val containerColor =
        if (permissionItem.isGranted()) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.errorContainer
    Card(
        containerColor = containerColor,
        modifier = Modifier
            .padding(horizontal = Dimens.Margin.Medium, vertical = Dimens.Margin.Small)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = Dimens.Margin.Medium, vertical = Dimens.Margin.Small)
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
            Text(text = permissionItem.name.get(), style = MaterialTheme.typography.titleLarge)
            Icon(
                painter = rememberVectorPainter(image = Icons.Rounded.CheckCircle),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = permissionItem.permissionGrantedDescription.get(),
                modifier = Modifier.size(Dimens.Icon.Medium)
            )
        }
        Spacer(modifier = Modifier.height(Dimens.Margin.Small))
        Text(
            text = permissionItem.permissionGrantedDescription.get(),
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
            Text(text = permissionItem.name.get(), style = MaterialTheme.typography.titleLarge)
            Icon(
                painter = rememberVectorPainter(image = Icons.Rounded.Warning),
                tint = MaterialTheme.colorScheme.error,
                contentDescription = permissionItem.permissionNotGrantedDescription.get(),
                modifier = Modifier.size(Dimens.Icon.Medium)
            )
        }
        Spacer(modifier = Modifier.height(Dimens.Margin.Small))
        Text(
            text = permissionItem.description.get(),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Dimens.Margin.Small))
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onRequestPermission) {
                Text(text = permissionItem.buttonText.get())
            }
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = PreviewDimens.Medium.Width)
private fun GrantedPermissionItemPreview() {
    val uiModel = PermissionItemUiModel.Granted(
        name = Strings.Location.Background::Name,
        permissionGrantedDescription = Strings.Location.Background::PermissionGrantedDescription
    )
    ShuttleTheme {
        PermissionItem(uiModel, onRequestPermission = {})
    }
}

@Composable
@Preview(showBackground = true, widthDp = PreviewDimens.Medium.Width)
private fun NotGrantedPermissionItemPreview() {
    val uiModel = PermissionItemUiModel.NotGranted(
        name = Strings.Location.Background::Name,
        description = Strings.Location.Background::Description,
        permissionNotGrantedDescription = Strings.Location.Background::PermissionNotGrantedDescription,
        buttonText = Strings.Location::Action
    )
    ShuttleTheme {
        PermissionItem(uiModel, onRequestPermission = {})
    }
}
