package shuttle.icons.domain.usecase

import android.graphics.drawable.Drawable
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.IconPacksRepository

class GetIconDrawableForApp(
    private val getSystemIconForApp: GetSystemIconDrawableForApp,
    private val iconsPacksRepository: IconPacksRepository
) {

    suspend operator fun invoke(id: AppId, iconPackId: AppId): Drawable {
        val systemIcon = getSystemIconForApp(id)
        return iconsPacksRepository.getDrawableIcon(iconPackId = iconPackId, appId = id, defaultDrawable = systemIcon)
    }
}
