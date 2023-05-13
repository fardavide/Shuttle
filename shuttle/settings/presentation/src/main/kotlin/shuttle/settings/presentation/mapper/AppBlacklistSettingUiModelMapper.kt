package shuttle.settings.presentation.mapper

import arrow.core.Either
import arrow.core.continuations.either
import org.koin.core.annotation.Factory
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp
import shuttle.settings.domain.model.AppBlacklistSetting
import shuttle.settings.presentation.model.AppBlacklistSettingUiModel

@Factory
class AppBlacklistSettingUiModelMapper(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp
) {

    suspend fun toUiModels(
        appBlacklistSettings: Collection<AppBlacklistSetting>
    ): List<Either<GetSystemIconError, AppBlacklistSettingUiModel>> =
        appBlacklistSettings.map { toUiModel(it) }

    private suspend fun toUiModel(
        appBlacklistSetting: AppBlacklistSetting
    ): Either<GetSystemIconError, AppBlacklistSettingUiModel> = either {
        val appModel = appBlacklistSetting.app
        AppBlacklistSettingUiModel(
            id = appModel.id,
            name = appModel.name.value,
            icon = getSystemIconDrawableForApp(appModel.id).bind(),
            isBlacklisted = appBlacklistSetting.inBlacklist
        )
    }
}
