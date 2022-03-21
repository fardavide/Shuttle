package shuttle.settings.presentation.mapper

import shuttle.apps.domain.model.AppModel
import shuttle.settings.presentation.model.WidgetPreviewAppUiModel
import shuttle.util.android.GetSystemIconDrawableForApp

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
