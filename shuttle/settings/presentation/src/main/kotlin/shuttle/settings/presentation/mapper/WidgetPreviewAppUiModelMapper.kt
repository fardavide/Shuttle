package shuttle.settings.presentation.mapper

import arrow.core.Either
import arrow.core.Option
import arrow.core.computations.either
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.icons.domain.usecase.GetIconDrawableForApp
import shuttle.settings.presentation.model.WidgetPreviewAppUiModel

class WidgetPreviewAppUiModelMapper(
    private val getIconDrawableForApp: GetIconDrawableForApp
) {

    suspend fun toUiModel(
        app: AppModel,
        iconPackId: Option<AppId>
    ): Either<GetSystemIconError, WidgetPreviewAppUiModel> = either {
        WidgetPreviewAppUiModel(
            name = app.name.value,
            icon = getIconDrawableForApp(id = app.id, iconPackId = iconPackId).bind()
        )
    }

    suspend fun toUiModels(
        apps: Collection<AppModel>,
        iconPackId: Option<AppId>
    ): List<Either<GetSystemIconError, WidgetPreviewAppUiModel>> =
        apps.map { toUiModel(app = it, iconPackId = iconPackId) }
}
