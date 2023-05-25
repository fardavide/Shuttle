package shuttle.permissions.presentation.mapper

import com.google.accompanist.permissions.MultiplePermissionsState
import org.koin.core.annotation.Factory
import shuttle.permissions.domain.usecase.HasBackgroundLocation
import shuttle.permissions.domain.usecase.HasCoarseLocation
import shuttle.permissions.domain.usecase.HasFineLocation
import shuttle.permissions.presentation.model.PermissionItem
import shuttle.permissions.presentation.model.PermissionItemsUiModel

@Factory
internal class PermissionItemsUiModelMapper(
    private val hasBackgroundLocation: HasBackgroundLocation,
    private val hasCoarseLocation: HasCoarseLocation,
    private val hasFineLocation: HasFineLocation
) {

    fun toUiModel(
        permissionsState: MultiplePermissionsState,
        isAccessibilityServiceEnabled: Boolean
    ): PermissionItemsUiModel {
        val coarseLocation =
            if (hasCoarseLocation(permissionsState)) PermissionItem.Location.Coarse.Granted
            else PermissionItem.Location.Coarse.NotGranted

        val fineLocation =
            if (hasFineLocation(permissionsState)) PermissionItem.Location.Fine.Granted
            else PermissionItem.Location.Fine.NotGranted

        val backgroundLocation =
            if (hasBackgroundLocation(permissionsState)) PermissionItem.Location.Background.Granted
            else PermissionItem.Location.Background.NotGranted

        val accessibilityService =
            if (isAccessibilityServiceEnabled) PermissionItem.Accessibility.Granted
            else PermissionItem.Accessibility.NotGranted

        return PermissionItemsUiModel(
            coarseLocation = coarseLocation,
            fineLocation = fineLocation,
            backgroundLocation = backgroundLocation,
            accessibilityService = accessibilityService
        )
    }
}
