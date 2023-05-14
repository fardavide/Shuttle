package shuttle.permissions.presentation.state

import shuttle.permissions.presentation.model.PermissionItemsUiModel

internal sealed interface PermissionsState {

    object Loading : PermissionsState

    object AllGranted : PermissionsState

    data class Pending(val permissionItemsUiModel: PermissionItemsUiModel) : PermissionsState
}
