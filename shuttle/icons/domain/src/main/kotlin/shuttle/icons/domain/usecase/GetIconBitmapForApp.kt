package shuttle.icons.domain.usecase

import android.graphics.Bitmap
import arrow.core.Option
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.IconPacksRepository

class GetIconBitmapForApp(
    private val getSystemIconBitmapForApp: GetSystemIconBitmapForApp,
    private val iconsPacksRepository: IconPacksRepository
) {

    suspend operator fun invoke(id: AppId, iconPackId: Option<AppId>): Bitmap {
        val systemIcon = getSystemIconBitmapForApp(id)
        val bitmap = iconPackId.fold(
            ifEmpty = { systemIcon },
            ifSome = {
                iconsPacksRepository.getBitmapIcon(
                    iconPackId = it,
                    appId = id,
                    defaultBitmap = systemIcon
                )
            }
        )
        return Bitmap.createBitmap(bitmap)
    }
}
