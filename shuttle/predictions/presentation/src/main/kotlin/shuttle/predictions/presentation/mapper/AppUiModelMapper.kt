package shuttle.predictions.presentation.mapper

import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp
import shuttle.predictions.presentation.model.AppUiModel

class AppUiModelMapper(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp
) {

    suspend fun toUiModel(appModel: SuggestedAppModel) = AppUiModel(
        id = appModel.id,
        name = appModel.name.value,
        icon = getSystemIconDrawableForApp(appModel.id)
    )

    suspend fun toUiModels(appModels: Collection<SuggestedAppModel>): List<AppUiModel> =
        appModels.map { toUiModel(it) }
}
