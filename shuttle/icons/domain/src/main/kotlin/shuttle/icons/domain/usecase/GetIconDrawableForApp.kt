package shuttle.icons.domain.usecase

import android.graphics.drawable.Drawable
import arrow.core.Option
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.IconPacksRepository

class GetIconDrawableForApp(
    private val getSystemIconForApp: GetSystemIconDrawableForApp,
    private val iconsPacksRepository: IconPacksRepository
) {

    suspend operator fun invoke(id: AppId, iconPackId: Option<AppId>): Drawable {
        val systemIcon = getSystemIconForApp(id)
        return iconPackId.fold(
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
