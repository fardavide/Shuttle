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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import shuttle.design.theme.ShuttleTheme
import shuttle.design.ui.LoadingSpinner
import shuttle.design.util.collectAsStateLifecycleAware
import shuttle.permissions.presentation.action.PermissionsAction
import shuttle.permissions.presentation.model.PermissionItem
import shuttle.permissions.presentation.model.PermissionItemsUiModel
import shuttle.permissions.presentation.state.PermissionsState
import shuttle.permissions.presentation.util.openAccessibilitySettings
import shuttle.permissions.presentation.viewmodel.PermissionsViewModel
import shuttle.resources.R.string

@Composable
fun PermissionsPage(toSettings: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val providedValues = arrayOf(
        LocalContext provides context,
        LocalLifecycleOwner provides lifecycleOwner
    )
    val viewModel: PermissionsViewModel = koinViewModel { parametersOf(providedValues) }

    val state = viewModel.state.collectAsStateLifecycleAware().value

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

    when (state) {
        PermissionsState.Loading -> LoadingSpinner()
        PermissionsState.AllGranted -> LaunchedEffect(state) {
            toSettings()
        }

        is PermissionsState.Pending -> PermissionsPageContent(
            permissions = state.permissionItemsUiModel,
            actions = PermissionsPage.Actions(
                onRequestLocation = { viewModel.submit(PermissionsAction.RequestLocation) },
                onRequestBackgroundLocation = { viewModel.submit(PermissionsAction.RequestBackgroundLocation) },
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
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = { Text(text = stringResource(id = string.permissions_title)) }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = actions.toSettings) {
                Text(text = stringResource(id = string.permissions_skip_permissions_action))
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
