package shuttle.predictions.presentation.mapper

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import shuttle.predictions.presentation.model.LocationPermissionsState
import shuttle.predictions.presentation.model.LocationPermissionsState.AllGranted
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.AllDenied
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.CoarseOnly
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.Init

@OptIn(ExperimentalPermissionsApi::class)
class LocationPermissionsStateMapper {

    internal fun toLocationPermissionState(permissionsState: MultiplePermissionsState): LocationPermissionsState =
        when {
            permissionsState.allPermissionsGranted -> AllGranted
            permissionsState.allPermissionsRevoked.not() -> CoarseOnly
            permissionsState.shouldShowRationale -> AllDenied
            else -> Init
        }

    private val MultiplePermissionsState.allPermissionsRevoked get() =
        permissions.size == revokedPermissions.size
}
