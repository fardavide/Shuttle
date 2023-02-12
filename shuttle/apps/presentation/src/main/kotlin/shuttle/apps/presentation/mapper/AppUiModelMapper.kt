package shuttle.apps.presentation.mapper

import arrow.core.Either
import arrow.core.continuations.either
import shuttle.apps.domain.model.AppModel
import shuttle.apps.presentation.model.AppUiModel
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp

class AppUiModelMapper(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp
) {

    suspend fun toUiModels(appModels: Collection<AppModel>): List<Either<GetSystemIconError, AppUiModel>> =
        appModels.map { toUiModel(it) }

    private suspend fun toUiModel(appModel: AppModel): Either<GetSystemIconError, AppUiModel> = either {
        AppUiModel(
            id = appModel.id,
            name = appModel.name.value,
            icon = getSystemIconDrawableForApp(appModel.id).bind()
        )
    }
}
