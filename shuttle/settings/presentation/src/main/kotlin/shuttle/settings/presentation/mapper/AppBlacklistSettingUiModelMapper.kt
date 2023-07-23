package shuttle.settings.presentation.mapper

import arrow.core.Either
import arrow.core.raise.either
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.GetAppError
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp
import shuttle.settings.domain.model.AppBlacklistSetting
import shuttle.settings.presentation.model.AppBlacklistSettingUiModel

@Factory
class AppBlacklistSettingUiModelMapper(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp
) {

    suspend fun toUiModels(
        appBlacklistSettings: Collection<AppBlacklistSetting>
    ): List<Either<GetAppError, AppBlacklistSettingUiModel>> = appBlacklistSettings.map { toUiModel(it) }

    private suspend fun toUiModel(
        appBlacklistSetting: AppBlacklistSetting
    ): Either<GetAppError, AppBlacklistSettingUiModel> = either {
        val appModel = appBlacklistSetting.app
        AppBlacklistSettingUiModel(
            id = appModel.id,
            name = appModel.name.value,
            icon = getSystemIconDrawableForApp(appModel.id).bind(),
            isBlacklisted = appBlacklistSetting.inBlacklist
        )
    }
}
