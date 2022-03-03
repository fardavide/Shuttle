package shuttle.permissions

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import shuttle.permissions.viewmodel.PermissionsViewModel

val permissionsModule = module {

    viewModel { PermissionsViewModel() }
}
