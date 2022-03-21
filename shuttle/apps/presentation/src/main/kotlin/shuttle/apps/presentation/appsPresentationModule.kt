package shuttle.apps.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import shuttle.apps.presentation.mapper.AppUiModelMapper
import shuttle.apps.presentation.viewmodel.AllAppsListViewModel

val appsPresentationModule = module {

    factory { AppUiModelMapper(getSystemIconDrawableForApp = get()) }
    viewModel { AllAppsListViewModel(appUiModelMapper = get(), observeAllInstalledApps = get()) }
}
