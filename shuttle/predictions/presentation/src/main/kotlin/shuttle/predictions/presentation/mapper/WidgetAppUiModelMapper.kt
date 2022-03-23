package shuttle.predictions.presentation.mapper

import android.graphics.Bitmap
import arrow.core.Option
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.icons.domain.usecase.GetIconBitmapForApp
import shuttle.predictions.presentation.model.WidgetAppUiModel
import shuttle.util.android.GetLaunchIntentForApp

class WidgetAppUiModelMapper(
    private val getIconBitmapForApp: GetIconBitmapForApp,
    private val getLaunchIntentForApp: GetLaunchIntentForApp
) {

    suspend fun toUiModel(appModel: SuggestedAppModel, iconPackId: Option<AppId>) = WidgetAppUiModel(
        id = appModel.id,
        name = appModel.name.value,
        icon = getIconBitmapForApp(id = appModel.id, iconPackId = iconPackId).setTint(appModel.isSuggested),
        launchIntent = getLaunchIntentForApp(appModel.id)
    )

    suspend fun toUiModels(
        appModels: Collection<SuggestedAppModel>,
        iconPackId: Option<AppId>
    ): List<WidgetAppUiModel> =
        appModels.map { toUiModel(appModel = it, iconPackId = iconPackId) }

    private fun Bitmap.setTint(isSuggested: Boolean) = apply {
//        if (isSuggested.not()) {
//            setTint(Color.parseColor("#B3CCCCCC"))
//            setTintMode(PorterDuff.Mode.SRC_ATOP)
//        }
    }
}
