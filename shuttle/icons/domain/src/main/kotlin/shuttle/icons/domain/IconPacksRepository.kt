package shuttle.icons.domain

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import shuttle.apps.domain.model.AppId

interface IconPacksRepository {

    suspend fun getDrawableIcon(iconPackId: AppId, appId: AppId, defaultDrawable: Drawable): Drawable

    suspend fun getBitmapIcon(iconPackId: AppId, appId: AppId, defaultBitmap: Bitmap): Bitmap

    suspend fun getIcon(iconPackId: AppId, appId: AppId, defaultIcon: Icon): Icon
}
