package shuttle.icons.domain.usecase

import android.graphics.Bitmap
import arrow.core.Either
import arrow.core.Option
import arrow.core.continuations.either
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.IconPacksRepository
import shuttle.icons.domain.error.GetSystemIconError

class GetIconBitmapForApp(
    private val getSystemIconBitmapForApp: GetSystemIconBitmapForApp,
    private val iconsPacksRepository: IconPacksRepository
) {

    suspend operator fun invoke(id: AppId, iconPackId: Option<AppId>): Either<GetSystemIconError, Bitmap> = either {
        val systemIcon = getSystemIconBitmapForApp(id).bind()
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
        Bitmap.createBitmap(bitmap)
    }
}
