package shuttle.permissions.mapper

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import shuttle.permissions.model.LocationPermissionsState
import shuttle.permissions.model.LocationPermissionsState.AllGranted
import shuttle.permissions.model.LocationPermissionsState.Pending.CoarseOnly
import shuttle.permissions.model.LocationPermissionsState.Pending.Init
import shuttle.permissions.model.LocationPermissionsState.Pending.MissingBackground
import shuttle.util.android.IsAndroidQ

@OptIn(ExperimentalPermissionsApi::class)
class LocationPermissionsStateMapper(
    private val isAndroidQ: IsAndroidQ
) {

    internal fun toLocationPermissionState(permissionsState: MultiplePermissionsState): LocationPermissionsState {
        val hasCoarse =
            permissionsState.permissions.any { it.permission == ACCESS_COARSE_LOCATION && it.status.isGranted }

        val hasFine =
            permissionsState.permissions.any { it.permission == ACCESS_FINE_LOCATION && it.status.isGranted }

        @SuppressWarnings("InlinedApi")
        val hasBackground = isAndroidQ().not() ||
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
