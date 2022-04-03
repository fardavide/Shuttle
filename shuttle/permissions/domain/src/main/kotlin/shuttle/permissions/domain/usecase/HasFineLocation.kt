package shuttle.permissions.domain.usecase

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import shuttle.permissions.domain.model.FineLocation

@OptIn(ExperimentalPermissionsApi::class)
class HasFineLocation {

    operator fun invoke(state: MultiplePermissionsState) =
        state.permissions.any { it.permission == FineLocation && it.status == PermissionStatus.Granted }
}
