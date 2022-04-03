package shuttle.permissions

import android.content.Context
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import shuttle.permissions.mapper.PermissionItemsUiModelMapper
import shuttle.permissions.viewmodel.PermissionsViewModel

val permissionsModule = module {

    factory { get<Context>().contentResolver }
    factory { PermissionItemsUiModelMapper(isAndroidQ = get()) }
    viewModel {
        PermissionsViewModel(
            isLaunchCounterServiceEnabled = get(),
            permissionItemsUiModelMapper = get()
        )
    }
}
