package shuttle.apps.presentation

import android.content.Context
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import shuttle.apps.presentation.util.GetIconForApp
import shuttle.apps.presentation.viewmodel.AllAppsListViewModel

val appsPresentationModule = module {

    factory { GetIconForApp(packageManager = get()) }
    viewModel { AllAppsListViewModel(getAllInstalledApps = get(), getIconForApp = get()) }
}
