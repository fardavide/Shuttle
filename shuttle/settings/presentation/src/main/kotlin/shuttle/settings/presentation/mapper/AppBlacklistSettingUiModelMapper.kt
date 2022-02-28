package shuttle.settings.presentation.mapper

import shuttle.settings.domain.model.AppBlacklistSetting
import shuttle.settings.presentation.model.AppBlacklistSettingUiModel
import shuttle.util.android.GetIconDrawableForApp

class AppBlacklistSettingUiModelMapper(
    private val getIconDrawableForApp: GetIconDrawableForApp
) {

    fun toUiModel(appBlacklistSetting: AppBlacklistSetting): AppBlacklistSettingUiModel {
        val appModel = appBlacklistSetting.app
        return AppBlacklistSettingUiModel(
            id = appModel.id,
            name = appModel.name.value,
            icon = getIconDrawableForApp(appModel.id),
            isBlacklisted = appBlacklistSetting.inBlacklist
        )
    }

    fun toUiModels(appBlacklistSettings: Collection<AppBlacklistSetting>): List<AppBlacklistSettingUiModel> =
        appBlacklistSettings.map(::toUiModel)
}
