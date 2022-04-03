package shuttle.permissions.domain.usecase

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import shuttle.permissions.domain.model.CoarseLocation

@OptIn(ExperimentalPermissionsApi::class)
class HasCoarseLocation {

    operator fun invoke(state: MultiplePermissionsState) =
        state.permissions.any { it.permission == CoarseLocation && it.status == PermissionStatus.Granted }
}
