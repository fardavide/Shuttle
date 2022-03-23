package shuttle.predictions.presentation

import kotlinx.coroutines.MainScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import shuttle.predictions.presentation.mapper.AppUiModelMapper
import shuttle.predictions.presentation.mapper.WidgetAppUiModelMapper
import shuttle.predictions.presentation.mapper.WidgetSettingsUiModelMapper
import shuttle.predictions.presentation.viewmodel.SuggestedAppsListViewModel
import shuttle.predictions.presentation.viewmodel.SuggestedAppsWidgetViewModel

val predictionsPresentationModule = module {

    viewModel {
        SuggestedAppsListViewModel(
            appUiModelMapper = get(),
            observeSuggestedApps = get(),
            packageManager = get()
        )
    }
    factory { AppUiModelMapper(getSystemIconDrawableForApp = get()) }
    factory {
        SuggestedAppsWidgetViewModel(
            appUiModelMapper = get(),
            observeCurrentIconPack = get(),
            observeSuggestedApps = get(),
            observeWidgetSettings = get(),
            widgetSettingsUiModelMapper = get(),
            viewModelScope = MainScope()
        )
    }
    factory { WidgetAppUiModelMapper(getIconForApp = get(), getLaunchIntentForApp = get()) }
    factory { WidgetSettingsUiModelMapper() }

}
