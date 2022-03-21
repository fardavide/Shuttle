package shuttle.icons.domain.usecase

import android.graphics.drawable.Icon
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.IconPacksRepository

class GetIconForApp(
    private val getSystemIconForApp: GetSystemIconForApp,
    private val iconPacksRepository: IconPacksRepository,
) {

    suspend operator fun invoke(id: AppId, iconPackId: AppId): Icon {
        val systemIcon = getSystemIconForApp(id)
        return iconPacksRepository.getIcon(iconPackId = iconPackId, appId = id, defaultIcon = systemIcon)
    }
}
