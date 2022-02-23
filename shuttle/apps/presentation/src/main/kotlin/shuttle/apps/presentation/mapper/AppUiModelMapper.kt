package shuttle.apps.presentation.mapper

import shuttle.apps.domain.model.AppModel
import shuttle.apps.presentation.model.AppUiModel
import shuttle.apps.presentation.util.GetIconForApp

class AppUiModelMapper(
    private val getIconForApp: GetIconForApp
) {

    fun toUiModel(appModel: AppModel) = AppUiModel(
        id = appModel.id,
        name = appModel.name.value,
        icon = getIconForApp(appModel.id)
    )

    fun toUiModels(appModels: Collection<AppModel>): List<AppUiModel> =
        appModels.map(::toUiModel)
}
