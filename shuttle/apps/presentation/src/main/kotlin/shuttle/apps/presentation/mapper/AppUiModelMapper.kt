package shuttle.apps.presentation.mapper

import arrow.core.Either
import arrow.core.raise.either
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.GetAppError
import shuttle.apps.presentation.model.AppUiModel
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp

@Factory
class AppUiModelMapper(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp
) {

    suspend fun toUiModels(appModels: Collection<AppModel>): List<Either<GetAppError, AppUiModel>> =
        appModels.map { toUiModel(it) }

    private suspend fun toUiModel(appModel: AppModel): Either<GetAppError, AppUiModel> = either {
        AppUiModel(
            id = appModel.id,
            name = appModel.name.value,
            icon = getSystemIconDrawableForApp(appModel.id).bind()
        )
    }
}
