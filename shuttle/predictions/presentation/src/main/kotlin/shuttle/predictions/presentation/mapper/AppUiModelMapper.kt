package shuttle.predictions.presentation.mapper

import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.predictions.presentation.model.AppUiModel
import shuttle.util.android.GetIconDrawableForApp

class AppUiModelMapper(
    private val getIconDrawableForApp: GetIconDrawableForApp
) {

    fun toUiModel(appModel: SuggestedAppModel) = AppUiModel(
        id = appModel.id,
        name = appModel.name.value,
        icon = getIconDrawableForApp(appModel.id)
    )

    fun toUiModels(appModels: Collection<SuggestedAppModel>): List<AppUiModel> =
        appModels.map(::toUiModel)
}
