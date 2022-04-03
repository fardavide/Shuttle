package shuttle.permissions.domain.usecase

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import shuttle.permissions.domain.model.BackgroundLocation
import shuttle.util.android.IsAndroidQ

@ExperimentalPermissionsApi
class HasBackgroundLocation(
    private val isAndroidQ: IsAndroidQ
) {

    operator fun invoke(state: MultiplePermissionsState) =
        isAndroidQ().not() ||
            state.permissions.any { it.permission == BackgroundLocation && it.status == PermissionStatus.Granted }
}

