package shuttle.predictions.presentation.mapper

import shuttle.apps.domain.model.AppModel
import shuttle.predictions.presentation.model.WidgetAppUiModel
import shuttle.util.android.GetIconForApp
import shuttle.util.android.GetLaunchIntentForApp

class WidgetAppUiModelMapper(
    private val getIconForApp: GetIconForApp,
    private val getLaunchIntentForApp: GetLaunchIntentForApp
) {

    fun toUiModel(appModel: AppModel) = WidgetAppUiModel(
        id = appModel.id,
        name = appModel.name.value,
        icon = getIconForApp(appModel.id),
        launchIntent = getLaunchIntentForApp(appModel.id)
    )

    fun toUiModels(appModels: Collection<AppModel>): List<WidgetAppUiModel> =
        appModels.map(::toUiModel)
}
