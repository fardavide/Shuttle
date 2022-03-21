package shuttle.settings.presentation.mapper

import shuttle.settings.domain.model.AppBlacklistSetting
import shuttle.settings.presentation.model.AppBlacklistSettingUiModel
import shuttle.util.android.GetSystemIconDrawableForApp

class AppBlacklistSettingUiModelMapper(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp
) {

    fun toUiModel(appBlacklistSetting: AppBlacklistSetting): AppBlacklistSettingUiModel {
        val appModel = appBlacklistSetting.app
        return AppBlacklistSettingUiModel(
            id = appModel.id,
            name = appModel.name.value,
            icon = getSystemIconDrawableForApp(appModel.id),
            isBlacklisted = appBlacklistSetting.inBlacklist
        )
    }

    fun toUiModels(appBlacklistSettings: Collection<AppBlacklistSetting>): List<AppBlacklistSettingUiModel> =
        appBlacklistSettings.map(::toUiModel)
}
