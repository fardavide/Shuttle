package shuttle.predictions.presentation.mapper

import arrow.core.Either
import arrow.core.continuations.either
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp
import shuttle.predictions.presentation.model.AppUiModel

@Factory
class AppUiModelMapper(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp
) {

    suspend fun toUiModel(appModel: SuggestedAppModel): Either<GetSystemIconError, AppUiModel> = either {
        AppUiModel(
            id = appModel.id,
            name = appModel.name.value,
            icon = getSystemIconDrawableForApp(appModel.id).bind()
        )
    }

    suspend fun toUiModels(
        appModels: Collection<SuggestedAppModel>
    ): List<Either<GetSystemIconError, AppUiModel>> = appModels.map { toUiModel(it) }
}
