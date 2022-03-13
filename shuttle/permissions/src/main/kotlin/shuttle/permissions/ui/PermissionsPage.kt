package shuttle.permissions.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.koin.androidx.compose.viewModel
import shuttle.design.ui.LoadingSpinner
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.permissions.model.PermissionItemsUiModel
import shuttle.permissions.model.backgroundPermissionsList
import shuttle.permissions.model.foregroundPermissionsList
import shuttle.permissions.util.openAccessibilitySettings
import shuttle.permissions.util.openLocationPermissionsOrAppSettings
import shuttle.permissions.viewmodel.PermissionsViewModel
import shuttle.permissions.viewmodel.PermissionsViewModel.Action
import shuttle.permissions.viewmodel.PermissionsViewModel.State

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun PermissionsPage(onAllPermissionsGranted: () -> Unit) {
    val context = LocalContext.current
    val viewModel: PermissionsViewModel by viewModel()

    val foregroundLocationPermissionsState = rememberMultiplePermissionsState(foregroundPermissionsList)
    val backgroundLocationPermissionsState = rememberMultiplePermissionsState(backgroundPermissionsList)

    viewModel.submit(Action.UpdatePermissionsState(backgroundLocationPermissionsState))
    val s by viewModel.state.collectAsStateLifecycleAware()

    @Suppress("UnnecessaryVariable")
    when (val state = s) {
        State.Loading -> LoadingSpinner()
        State.AllGranted -> LaunchedEffect(state) {
            onAllPermissionsGranted()
        }
        is State.Pending -> PermissionsList(
            permissions = state.permissionItemsUiModel,
            onRequestLocation = foregroundLocationPermissionsState::launchMultiplePermissionRequest,
            onRequestBackgroundLocation = { openLocationPermissionsOrAppSettings(context) },
            onRequestAccessibilityService = { openAccessibilitySettings(context) }
        )
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
