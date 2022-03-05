package shuttle.settings.presentation.mapper

import shuttle.apps.domain.model.AppModel
import shuttle.settings.presentation.model.WidgetPreviewAppUiModel
import shuttle.util.android.GetIconDrawableForApp

class WidgetPreviewAppUiModelMapper(
    private val getIconDrawableForApp: GetIconDrawableForApp
) {

    fun toUiModel(app: AppModel) = WidgetPreviewAppUiModel(
        name = app.name.value,
        icon = getIconDrawableForApp(app.id)
    )

    fun toUiModels(apps: Collection<AppModel>): List<WidgetPreviewAppUiModel> =
        apps.map(::toUiModel)
}
