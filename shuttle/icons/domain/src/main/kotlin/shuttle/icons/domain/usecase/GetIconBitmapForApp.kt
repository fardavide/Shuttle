package shuttle.icons.domain.usecase

import android.graphics.Bitmap
import arrow.core.Either
import arrow.core.Option
import arrow.core.raise.either
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.GetAppError
import shuttle.icons.domain.repository.IconPacksRepository

@Factory
class GetIconBitmapForApp(
    private val getSystemIconBitmapForApp: GetSystemIconBitmapForApp,
    private val iconsPacksRepository: IconPacksRepository
) {

    suspend operator fun invoke(id: AppId, iconPackId: Option<AppId>): Either<GetAppError, Bitmap> = either {
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
