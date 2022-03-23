package shuttle.icons.domain.usecase

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import shuttle.apps.domain.model.AppId

class GetSystemIconBitmapForApp(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(id: AppId): Bitmap =
        withContext(ioDispatcher) {
            val drawable = getSystemIconDrawableForApp(id)
            Bitmap.createBitmap(toBitmap(drawable))
        }

    private fun toBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }
        val bitmap: Bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            // Single color bitmap will be created of 1x1 pixel
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
