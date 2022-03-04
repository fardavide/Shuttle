package shuttle.permissions

import android.content.ComponentName
import android.content.Context
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import shuttle.accessibility.LaunchCounterAccessibilityService
import shuttle.permissions.mapper.LocationPermissionsStateMapper
import shuttle.permissions.viewmodel.PermissionsViewModel

val permissionsModule = module {

    factory { get<Context>().contentResolver }
    factory { LocationPermissionsStateMapper() }
    viewModel {
        PermissionsViewModel(
            accessibilityServiceComponentName = ComponentName(get(), LaunchCounterAccessibilityService::class.java),
            contentResolver = get(),
            mapper = get(),
        )
    }
}
