package shuttle.permissions.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.koin.androidx.compose.koinViewModel
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.LoadingSpinner
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.permissions.domain.model.backgroundPermissionsList
import shuttle.permissions.domain.model.foregroundPermissionsList
import shuttle.permissions.presentation.model.PermissionItem
import shuttle.permissions.presentation.model.PermissionItemsUiModel
import shuttle.permissions.presentation.util.openAccessibilitySettings
import shuttle.permissions.presentation.viewmodel.PermissionsViewModel
import shuttle.permissions.presentation.viewmodel.PermissionsViewModel.Action
import shuttle.permissions.presentation.viewmodel.PermissionsViewModel.State
import shuttle.resources.R

@Composable
fun PermissionsPage(toSettings: () -> Unit) {
    val context = LocalContext.current
    val viewModel: PermissionsViewModel = koinViewModel()

    val foregroundLocationPermissionsState = rememberMultiplePermissionsState(foregroundPermissionsList)
    val backgroundLocationPermissionsState = rememberMultiplePermissionsState(backgroundPermissionsList)

    viewModel.submit(Action.UpdatePermissionsState(backgroundLocationPermissionsState))
    val s by viewModel.state.collectAsStateLifecycleAware()

    var shouldShowAccessibilityServiceDialog by remember { mutableStateOf(false) }
    if (shouldShowAccessibilityServiceDialog) {
        AccessibilityServiceDialog(
            AccessibilityServiceDialog.Actions(
                onConfirm = {
                    openAccessibilitySettings(context)
                    shouldShowAccessibilityServiceDialog = false
                },
                onDismiss = { shouldShowAccessibilityServiceDialog = false }
            )
        )
    }

    when (val state = s) {
        State.Loading -> LoadingSpinner()
        State.AllGranted -> LaunchedEffect(state) {
            toSettings()
        }

        is State.Pending -> PermissionsPageContent(
            permissions = state.permissionItemsUiModel,
            actions = PermissionsPage.Actions(
                onRequestLocation = foregroundLocationPermissionsState::launchMultiplePermissionRequest,
                onRequestBackgroundLocation = backgroundLocationPermissionsState::launchMultiplePermissionRequest,
                onRequestAccessibilityService = { shouldShowAccessibilityServiceDialog = true },
                toSettings = toSettings
            )
        )
    }
}

@Composable
private fun PermissionsPageContent(
    permissions: PermissionItemsUiModel,
    actions: PermissionsPage.Actions,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .testTag(PermissionsPage.TEST_TAG)
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.permissions_title)) }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = actions.toSettings) {
                Text(text = stringResource(id = R.string.permissions_skip_permissions_action))
            }
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            PermissionsList(permissions, actions)
        }
    }
}

@Composable
private fun PermissionsList(permissions: PermissionItemsUiModel, actions: PermissionsPage.Actions) {
    Column {
        PermissionItem(
            permissionItem = permissions.coarseLocation,
            onRequestPermission = actions.onRequestLocation
        )
        PermissionItem(
            permissionItem = permissions.fineLocation,
            onRequestPermission = actions.onRequestLocation
        )
        PermissionItem(
            permissionItem = permissions.backgroundLocation,
            onRequestPermission = actions.onRequestBackgroundLocation
        )
        PermissionItem(
            permissionItem = permissions.accessibilityService,
            onRequestPermission = actions.onRequestAccessibilityService
        )
    }
}

object PermissionsPage {

    const val TEST_TAG = "PermissionsPage"

    data class Actions(
        val onRequestLocation: () -> Unit,
        val onRequestBackgroundLocation: () -> Unit,
        val onRequestAccessibilityService: () -> Unit,
        val toSettings: () -> Unit
    ) {

        companion object {

            val Empty = Actions(
                onRequestLocation = {},
                onRequestBackgroundLocation = {},
                onRequestAccessibilityService = {},
                toSettings = {}
            )
        }
    }
}

@Composable
@Preview(showBackground = false)
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
            actions = PermissionsPage.Actions.Empty
        )
    }
}
