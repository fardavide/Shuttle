package shuttle.predictions.presentation.mapper

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Build
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import shuttle.predictions.presentation.model.LocationPermissionsState
import shuttle.predictions.presentation.model.LocationPermissionsState.AllGranted
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.CoarseOnly
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.Init
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.MissingBackground

@OptIn(ExperimentalPermissionsApi::class)
class LocationPermissionsStateMapper {

    internal fun toLocationPermissionState(permissionsState: MultiplePermissionsState): LocationPermissionsState {
        val hasCoarse =
            permissionsState.permissions.any { it.permission == ACCESS_COARSE_LOCATION && it.status.isGranted }

        val hasFine =
            permissionsState.permissions.any { it.permission == ACCESS_FINE_LOCATION && it.status.isGranted }

        val hasBackground = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q ||
            permissionsState.permissions.any { it.permission == ACCESS_BACKGROUND_LOCATION && it.status.isGranted }

        return when {
            hasCoarse && hasFine.not() -> CoarseOnly
            hasCoarse && hasFine && hasBackground.not() -> MissingBackground
            permissionsState.allPermissionsGranted -> AllGranted
            else -> Init
//            permissionsState.shouldShowRationale -> AllDenied
        }
    }
}
