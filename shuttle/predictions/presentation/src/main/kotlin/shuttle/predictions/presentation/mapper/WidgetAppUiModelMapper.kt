package shuttle.predictions.presentation.mapper

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.core.graphics.applyCanvas
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
        icon = getIconBitmapForApp(id = appModel.id, iconPackId = iconPackId).withTint(appModel.isSuggested),
        launchIntent = getLaunchIntentForApp(appModel.id)
    )

    suspend fun toUiModels(
        appModels: Collection<SuggestedAppModel>,
        iconPackId: Option<AppId>
    ): List<WidgetAppUiModel> =
        appModels.map { toUiModel(appModel = it, iconPackId = iconPackId) }

    private fun Bitmap.withTint(isSuggested: Boolean): Bitmap {
        return if (isSuggested.not()) {
            copy(Bitmap.Config.ARGB_8888, true).applyCanvas {
                val paint = Paint()
                val filter: ColorFilter =
                    PorterDuffColorFilter(Color.parseColor("#B3CCCCCC"), PorterDuff.Mode.SRC_IN)
                paint.colorFilter = filter

                drawBitmap(this@withTint, 0f, 0f, paint)
            }
        } else this
    }
}
