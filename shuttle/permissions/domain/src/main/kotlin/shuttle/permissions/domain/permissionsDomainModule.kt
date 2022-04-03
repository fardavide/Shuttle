package shuttle.permissions.domain

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import org.koin.dsl.module
import shuttle.permissions.domain.usecase.HasAllLocationPermissions
import shuttle.permissions.domain.usecase.HasBackgroundLocation
import shuttle.permissions.domain.usecase.HasCoarseLocation
import shuttle.permissions.domain.usecase.HasFineLocation

@OptIn(ExperimentalPermissionsApi::class)
val permissionsDomainModule = module {

    factory {
        HasAllLocationPermissions(
            hasBackgroundLocation = get(),
            hasCoarseLocation = get(),
            hasFineLocation = get()
        )
    }
    factory { HasBackgroundLocation(isAndroidQ = get()) }
    factory { HasCoarseLocation() }
    factory { HasFineLocation() }
}
