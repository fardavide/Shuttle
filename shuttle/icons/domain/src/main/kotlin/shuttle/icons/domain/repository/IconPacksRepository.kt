package shuttle.icons.domain.repository

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import shuttle.apps.domain.model.AppId

interface IconPacksRepository {

    suspend fun getDrawableIcon(
        iconPackId: AppId,
        appId: AppId,
        defaultDrawable: Drawable
    ): Drawable

    suspend fun getBitmapIcon(
        iconPackId: AppId,
        appId: AppId,
        defaultBitmap: Bitmap
    ): Bitmap
}
