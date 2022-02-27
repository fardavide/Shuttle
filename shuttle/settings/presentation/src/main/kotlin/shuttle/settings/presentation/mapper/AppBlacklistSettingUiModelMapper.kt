package shuttle.settings.presentation.mapper

import shuttle.apps.presentation.util.GetIconForApp
import shuttle.settings.domain.model.AppBlacklistSetting
import shuttle.settings.presentation.model.AppBlacklistSettingUiModel

class AppBlacklistSettingUiModelMapper(
    private val getIconForApp: GetIconForApp
) {

    fun toUiModel(appBlacklistSetting: AppBlacklistSetting): AppBlacklistSettingUiModel {
        val appModel = appBlacklistSetting.app
        return AppBlacklistSettingUiModel(
            id = appModel.id,
            name = appModel.name.value,
            icon = getIconForApp(appModel.id),
            isBlacklisted = appBlacklistSetting.inBlacklist
        )
    }

    fun toUiModels(appBlacklistSettings: Collection<AppBlacklistSetting>): List<AppBlacklistSettingUiModel> =
        appBlacklistSettings.map(::toUiModel)
}
