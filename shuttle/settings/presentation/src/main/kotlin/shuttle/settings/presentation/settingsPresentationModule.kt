package shuttle.settings.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import shuttle.settings.presentation.mapper.AppBlacklistSettingUiModelMapper
import shuttle.settings.presentation.mapper.IconPackSettingsUiModelMapper
import shuttle.settings.presentation.mapper.WidgetPreviewAppUiModelMapper
import shuttle.settings.presentation.mapper.WidgetSettingsUiModelMapper
import shuttle.settings.presentation.viewmodel.BlacklistSettingsViewModel
import shuttle.settings.presentation.viewmodel.IconPacksSettingsViewModel
import shuttle.settings.presentation.viewmodel.WidgetSettingsViewModel

val settingsPresentationModule = module {

    viewModel {
        BlacklistSettingsViewModel(
            appUiModelMapper = get(),
            observeAppsBlacklistSettings = get(),
            addToBlacklist = get(),
            removeFromBlacklist = get()
        )
    }
    viewModel {
        IconPacksSettingsViewModel(
            iconPackSettingsMapper = get(),
            observeInstalledIconPacks = get(),
            observeCurrentIconPack = get(),
            setCurrentIconPack = get()
        )
    }
    viewModel {
        WidgetSettingsViewModel(
            observeAllInstalledApps = get(),
            observeWidgetSettings = get(),
            updateWidgetSettings = get(),
            widgetPreviewAppUiModelMapper = get(),
            widgetSettingsUiModelMapper = get()
        )
    }
    factory { AppBlacklistSettingUiModelMapper(getSystemIconDrawableForApp = get()) }
    factory { IconPackSettingsUiModelMapper(getSystemIconDrawableForApp = get()) }
    factory { WidgetPreviewAppUiModelMapper(getSystemIconDrawableForApp = get()) }
    factory { WidgetSettingsUiModelMapper() }
}
