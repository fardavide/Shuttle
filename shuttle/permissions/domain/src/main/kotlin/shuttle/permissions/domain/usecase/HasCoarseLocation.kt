package shuttle.permissions.domain.usecase

import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import org.koin.core.annotation.Factory
import shuttle.permissions.domain.model.CoarseLocation

@Factory
class HasCoarseLocation {

    operator fun invoke(state: MultiplePermissionsState) =
        state.permissions.any { it.permission == CoarseLocation && it.status == PermissionStatus.Granted }
}
