package shuttle.predictions.presentation.mapper

import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.predictions.presentation.model.AppUiModel
import shuttle.util.android.GetSystemIconDrawableForApp

class AppUiModelMapper(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp
) {

    fun toUiModel(appModel: SuggestedAppModel) = AppUiModel(
        id = appModel.id,
        name = appModel.name.value,
        icon = getSystemIconDrawableForApp(appModel.id)
    )

    fun toUiModels(appModels: Collection<SuggestedAppModel>): List<AppUiModel> =
        appModels.map(::toUiModel)
}
