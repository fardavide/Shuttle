package shuttle.onboarding.presentation.mapper

import arrow.core.Either
import arrow.core.continuations.either
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppModel
import shuttle.design.model.WidgetPreviewAppUiModel
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp

@Factory
class WidgetPreviewAppUiModelMapper(
    private val getIconDrawableForApp: GetSystemIconDrawableForApp
) {

    suspend fun toUiModels(apps: Collection<AppModel>): List<Either<GetSystemIconError, WidgetPreviewAppUiModel>> =
        apps.map { toUiModel(app = it) }

    private suspend fun toUiModel(app: AppModel): Either<GetSystemIconError, WidgetPreviewAppUiModel> = either {
        WidgetPreviewAppUiModel(
            name = app.name.value,
            icon = getIconDrawableForApp(id = app.id).bind()
        )
    }
}
