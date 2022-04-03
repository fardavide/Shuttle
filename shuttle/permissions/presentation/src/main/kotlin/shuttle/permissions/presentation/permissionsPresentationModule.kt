package shuttle.permissions

import android.content.Context
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import shuttle.permissions.presentation.mapper.PermissionItemsUiModelMapper
import shuttle.permissions.presentation.viewmodel.PermissionsViewModel

val permissionsPresentationModule = module {

    factory { get<Context>().contentResolver }
    factory { PermissionItemsUiModelMapper(isAndroidQ = get()) }
    viewModel {
        PermissionsViewModel(
            isLaunchCounterServiceEnabled = get(),
            permissionItemsUiModelMapper = get()
        )
    }
}
