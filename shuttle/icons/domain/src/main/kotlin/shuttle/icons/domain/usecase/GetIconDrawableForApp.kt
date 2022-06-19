package shuttle.icons.domain.usecase

import android.graphics.drawable.Drawable
import arrow.core.Either
import arrow.core.Option
import arrow.core.continuations.either
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.IconPacksRepository
import shuttle.icons.domain.error.GetSystemIconError

class GetIconDrawableForApp(
    private val getSystemIconForApp: GetSystemIconDrawableForApp,
    private val iconsPacksRepository: IconPacksRepository
) {

    suspend operator fun invoke(id: AppId, iconPackId: Option<AppId>): Either<GetSystemIconError, Drawable> =
        either {
            val systemIcon = getSystemIconForApp(id).bind()
            iconPackId.fold(
                ifEmpty = { systemIcon },
                ifSome = {
                    iconsPacksRepository.getDrawableIcon(
                        iconPackId = it,
                        appId = id,
                        defaultDrawable = systemIcon
                    )
                }
            )
        }
}
