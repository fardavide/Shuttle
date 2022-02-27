package shuttle.settings.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import shuttle.settings.presentation.mapper.AppBlacklistSettingUiModelMapper
import shuttle.settings.presentation.viewmodel.BlacklistSettingsViewModel

val settingsPresentationModule = module {

    factory { AppBlacklistSettingUiModelMapper(getIconForApp = get()) }
    viewModel {
        BlacklistSettingsViewModel(
            appUiModelMapper = get(),
            observeAppsBlacklistSettings = get(),
            addToBlacklist = get(),
            removeFromBlacklist = get()
        )
    }
}
