package shuttle.predictions.presentation.mapper

import android.content.pm.PackageManager
import shuttle.apps.domain.model.AppModel
import shuttle.apps.presentation.util.GetIconForApp
import shuttle.predictions.presentation.model.WidgetAppUiModel

class WidgetAppUiModelMapper(
    private val getIconForApp: GetIconForApp,
    private val packageManager: PackageManager
) {

    fun toUiModel(appModel: AppModel) = WidgetAppUiModel(
        id = appModel.id,
        name = appModel.name.value,
        icon = getIconForApp(appModel.id),
        launchIntent = packageManager.getLaunchIntentForPackage(appModel.id.value)!!
    )

    fun toUiModels(appModels: Collection<AppModel>): List<WidgetAppUiModel> =
        appModels.map(::toUiModel)
}
