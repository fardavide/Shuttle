package shuttle.permissions.ui

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
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.koin.androidx.compose.viewModel
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.LoadingSpinner
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.permissions.model.PermissionItem
import shuttle.permissions.model.PermissionItemsUiModel
import shuttle.permissions.model.backgroundPermissionsList
import shuttle.permissions.model.foregroundPermissionsList
import shuttle.permissions.resources.Strings
import shuttle.permissions.resources.get
import shuttle.permissions.util.openAccessibilitySettings
import shuttle.permissions.util.openLocationPermissionsOrAppSettings
import shuttle.permissions.viewmodel.PermissionsViewModel
import shuttle.permissions.viewmodel.PermissionsViewModel.Action
import shuttle.permissions.viewmodel.PermissionsViewModel.State

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
        topBar = { SmallTopAppBar(title = { Text(text = Strings::Title.get()) }) }
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
                    Text(text = Strings::SkipPermissionsAction.get())
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
