package shuttle.permissions.mapper

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import shuttle.permissions.model.BackgroundLocation
import shuttle.permissions.model.CoarseLocation
import shuttle.permissions.model.FineLocation
import shuttle.permissions.model.PermissionItem
import shuttle.permissions.model.PermissionItemsUiModel
import shuttle.util.android.IsAndroidQ

@OptIn(ExperimentalPermissionsApi::class)
internal class PermissionItemsUiModelMapper(
    private val isAndroidQ: IsAndroidQ
) {

    fun toUiModel(
        permissionsState: MultiplePermissionsState,
        isAccessibilityServiceEnabled: Boolean
    ): PermissionItemsUiModel {

        val coarseLocation =
            if (permissionsState.hasCoarseLocation()) PermissionItem.Location.Coarse.Granted
            else PermissionItem.Location.Coarse.NotGranted

        val fineLocation =
            if (permissionsState.hasFineLocation()) PermissionItem.Location.Fine.Granted
            else PermissionItem.Location.Fine.NotGranted

        val backgroundLocation =
            if (permissionsState.hasBackgroundLocation()) PermissionItem.Location.Background.Granted
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

    private fun MultiplePermissionsState.hasCoarseLocation() =
        permissions.any { it.permission == CoarseLocation && it.status == PermissionStatus.Granted }

    private fun MultiplePermissionsState.hasFineLocation() =
        permissions.any { it.permission == FineLocation && it.status == PermissionStatus.Granted }

    private fun MultiplePermissionsState.hasBackgroundLocation() =
        isAndroidQ().not() ||
            permissions.any { it.permission == BackgroundLocation && it.status == PermissionStatus.Granted }
}
