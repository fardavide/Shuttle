package shuttle.icons.domain.usecase

import android.graphics.drawable.Drawable
import arrow.core.Either
import arrow.core.Option
import arrow.core.raise.either
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.icons.domain.repository.IconPacksRepository

@Factory
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
