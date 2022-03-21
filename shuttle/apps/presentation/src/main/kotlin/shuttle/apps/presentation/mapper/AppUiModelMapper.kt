package shuttle.apps.presentation.mapper

import shuttle.apps.domain.model.AppModel
import shuttle.apps.presentation.model.AppUiModel
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp

class AppUiModelMapper(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp
) {

    fun toUiModel(appModel: AppModel) = AppUiModel(
        id = appModel.id,
        name = appModel.name.value,
        icon = getSystemIconDrawableForApp(appModel.id)
    )

    fun toUiModels(appModels: Collection<AppModel>): List<AppUiModel> =
        appModels.map(::toUiModel)
}
