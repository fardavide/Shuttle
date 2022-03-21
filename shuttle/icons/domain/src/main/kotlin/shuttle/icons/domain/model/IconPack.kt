package shuttle.icons.domain.model

import android.content.res.Resources
import android.graphics.Bitmap
import shuttle.apps.domain.model.AppId

data class IconPack(
    val id: AppId,
    val resources: Resources,
    val drawables: Map<AppId, DrawableName>,
    val frontImage: Bitmap?,
    val backImages: List<Bitmap>,
    val maskImage: Bitmap?,
    val factor: Float
) {

    @JvmInline
    value class DrawableName(val value: String)
}
