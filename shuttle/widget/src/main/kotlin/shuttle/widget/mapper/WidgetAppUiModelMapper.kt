package shuttle.widget.mapper

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.core.graphics.applyCanvas
import arrow.core.Either
import arrow.core.Option
import arrow.core.raise.either
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.icons.domain.usecase.GetIconBitmapForApp
import shuttle.util.android.GetLaunchIntentForApp
import shuttle.widget.model.WidgetAppUiModel

@Factory
internal class WidgetAppUiModelMapper(
    private val getIconBitmapForApp: GetIconBitmapForApp,
    private val getLaunchIntentForApp: GetLaunchIntentForApp
) {

    suspend fun toUiModels(
        appModels: Collection<SuggestedAppModel>,
        iconPackId: Option<AppId>
    ): List<Either<GetSystemIconError, WidgetAppUiModel>> =
        appModels.map { toUiModel(appModel = it, iconPackId = iconPackId) }

    private suspend fun toUiModel(
        appModel: SuggestedAppModel,
        iconPackId: Option<AppId>
    ): Either<GetSystemIconError, WidgetAppUiModel> = either {
        WidgetAppUiModel(
            id = appModel.id,
            name = appModel.name.value,
            icon = getIconBitmapForApp(id = appModel.id, iconPackId = iconPackId).bind().withTint(appModel.isSuggested),
            launchIntent = getLaunchIntentForApp(appModel.id)
        )
    }

    private fun Bitmap.withTint(isSuggested: Boolean): Bitmap {
        return if (isSuggested.not()) {
            copy(Bitmap.Config.ARGB_8888, true).applyCanvas {
                val paint = Paint()
                paint.colorFilter = NotSuggestedColorFilter

                drawBitmap(this@withTint, 0f, 0f, paint)
            }
        } else {
            this
        }
    }

    companion object {

        val NotSuggestedColorFilter: ColorFilter =
            PorterDuffColorFilter(Color.parseColor("#B3CCCCCC"), PorterDuff.Mode.SRC_IN)
    }
}
