package shuttle.predictions.presentation.mapper

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Build
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import shuttle.predictions.presentation.model.LocationPermissionsState
import shuttle.predictions.presentation.model.LocationPermissionsState.AllGranted
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.AllDenied
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.CoarseOnly
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.Init
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.MissingBackground

@OptIn(ExperimentalPermissionsApi::class)
class LocationPermissionsStateMapper {

    internal fun toLocationPermissionState(permissionsState: MultiplePermissionsState): LocationPermissionsState {
        val hasFine = permissionsState.revokedPermissions.none { it.permission == ACCESS_FINE_LOCATION }
        val hasBackground = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q ||
            permissionsState.revokedPermissions.any { it.permission == ACCESS_BACKGROUND_LOCATION }
        return when {
            permissionsState.allPermissionsGranted -> AllGranted
            hasFine.not() -> CoarseOnly
            hasBackground.not() -> MissingBackground
            permissionsState.shouldShowRationale -> AllDenied
            else -> Init
        }
    }
}
