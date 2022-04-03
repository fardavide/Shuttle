package shuttle.permissions.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.koin.androidx.compose.viewModel
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.LoadingSpinner
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.permissions.domain.model.backgroundPermissionsList
import shuttle.permissions.domain.model.foregroundPermissionsList
import shuttle.permissions.presentation.model.PermissionItem
import shuttle.permissions.presentation.model.PermissionItemsUiModel
import shuttle.permissions.presentation.util.openAccessibilitySettings
import shuttle.permissions.presentation.util.openLocationPermissionsOrAppSettings
import shuttle.permissions.presentation.viewmodel.PermissionsViewModel
import shuttle.permissions.presentation.viewmodel.PermissionsViewModel.Action
import shuttle.permissions.presentation.viewmodel.PermissionsViewModel.State
import studio.forface.shuttle.design.R

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun PermissionsPage(toSettings: () -> Unit) {
    val context = LocalContext.current
    val viewModel: PermissionsViewModel by viewModel()

    val foregroundLocationPermissionsState = rememberMultiplePermissionsState(foregroundPermissionsList)
    val backgroundLocationPermissionsState = rememberMultiplePermissionsState(backgroundPermissionsList)

    viewModel.submit(Action.UpdatePermissionsState(backgroundLocationPermissionsState))
    val s by viewModel.state.collectAsStateLifecycleAware()

    var shouldShowAccessibilityServiceDialog by remember { mutableStateOf(false) }
    if (shouldShowAccessibilityServiceDialog) {
        AccessibilityServiceDialog(
            onConfirm = {
                openAccessibilitySettings(context)
                shouldShowAccessibilityServiceDialog = false
            },
            onDismiss = { shouldShowAccessibilityServiceDialog = false }
        )
    }

    @Suppress("UnnecessaryVariable")
    when (val state = s) {
        State.Loading -> LoadingSpinner()
        State.AllGranted -> LaunchedEffect(state) {
            toSettings()
        }
        is State.Pending -> PermissionsPageContent(
            permissions = state.permissionItemsUiModel,
            onRequestLocation = foregroundLocationPermissionsState::launchMultiplePermissionRequest,
            onRequestAccessibilityService = { shouldShowAccessibilityServiceDialog = true },
            toSettings = toSettings
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PermissionsPageContent(
    permissions: PermissionItemsUiModel,
    onRequestLocation: () -> Unit,
    onRequestAccessibilityService: () -> Unit,
    toSettings: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text(text = stringResource(id = R.string.permissions_title)) }) }
    ) {
        Column {
            PermissionsList(
                permissions = permissions,
                onRequestLocation = onRequestLocation,
                onRequestBackgroundLocation = { openLocationPermissionsOrAppSettings(context) },
                onRequestAccessibilityService = onRequestAccessibilityService
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.Margin.Large)
            ) {
                Button(onClick = toSettings) {
                    Text(text = stringResource(id = R.string.permissions_skip_permissions_action))
                }
            }
        }
    }
}

@Composable
private fun PermissionsList(
    permissions: PermissionItemsUiModel,
    onRequestLocation: () -> Unit,
    onRequestBackgroundLocation: () -> Unit,
    onRequestAccessibilityService: () -> Unit
) {
    LazyColumn {
        item {
            PermissionItem(
                permissionItem = permissions.coarseLocation,
                onRequestPermission = onRequestLocation
            )
        }
        item {
            PermissionItem(
                permissionItem = permissions.fineLocation,
                onRequestPermission = onRequestLocation
            )
        }
        item {
            PermissionItem(
                permissionItem = permissions.backgroundLocation,
                onRequestPermission = onRequestBackgroundLocation
            )
        }
        item {
            PermissionItem(
                permissionItem = permissions.accessibilityService,
                onRequestPermission = onRequestAccessibilityService
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PermissionsPageContentPreview() {
    val permissionItemsUiModel = PermissionItemsUiModel(
        coarseLocation = PermissionItem.Location.Coarse.Granted,
        fineLocation = PermissionItem.Location.Fine.Granted,
        backgroundLocation = PermissionItem.Location.Background.Granted,
        accessibilityService = PermissionItem.Accessibility.Granted
    )
    ShuttleTheme {
        PermissionsPageContent(
            permissions = permissionItemsUiModel,
            onRequestLocation = {},
            onRequestAccessibilityService = {},
            toSettings = {}
        )
    }
}
