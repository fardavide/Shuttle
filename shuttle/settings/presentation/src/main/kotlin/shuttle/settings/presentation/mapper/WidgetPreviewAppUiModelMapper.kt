package shuttle.settings.presentation.mapper

import shuttle.apps.domain.model.AppModel
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp
import shuttle.settings.presentation.model.WidgetPreviewAppUiModel

class WidgetPreviewAppUiModelMapper(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp
) {

    fun toUiModel(app: AppModel) = WidgetPreviewAppUiModel(
        name = app.name.value,
        icon = getSystemIconDrawableForApp(app.id)
    )

    fun toUiModels(apps: Collection<AppModel>): List<WidgetPreviewAppUiModel> =
        apps.map(::toUiModel)
}
