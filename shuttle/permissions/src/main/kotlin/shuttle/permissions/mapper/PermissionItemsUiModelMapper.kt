package shuttle.permissions.mapper

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import shuttle.permissions.model.PermissionItem
import shuttle.permissions.model.PermissionItemsUiModel
import shuttle.util.android.IsAndroidQ

@OptIn(ExperimentalPermissionsApi::class)
internal class PermissionItemsUiModelMapper(
    isAndroidQ: IsAndroidQ
) {

    fun toUiModel(
        permissionsState: MultiplePermissionsState,
        isAccessibilityServiceEnabled: Boolean
    ): PermissionItemsUiModel {

        val coarseLocation = PermissionItem.Location.Coarse.Granted
        val fineLocation = PermissionItem.Location.Fine.Granted
        val backgroundLocation = PermissionItem.Location.Background.Granted

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
