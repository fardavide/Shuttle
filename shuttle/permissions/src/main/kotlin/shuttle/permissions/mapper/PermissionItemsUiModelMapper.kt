package shuttle.permissions.mapper

import shuttle.permissions.model.LocationPermissionsState
import shuttle.permissions.model.PermissionItemUiModel
import shuttle.permissions.model.PermissionItemsUiModel

internal class PermissionItemsUiModelMapper {

    fun toUiModel(
        locationPermissionsState: LocationPermissionsState,
        isAccessibilityServiceEnabled: Boolean
    ): PermissionItemsUiModel {
        return PermissionItemsUiModel(
            coarseLocation = PermissionItemUiModel.Granted("", ""),
            fineLocation = PermissionItemUiModel.Granted("", ""),
            backgroundLocation = PermissionItemUiModel.Granted("", ""),
            accessibilityService = PermissionItemUiModel.Granted("", ""),
        )
    }
}
