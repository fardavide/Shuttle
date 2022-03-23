package shuttle.icons.domain.usecase

import android.graphics.drawable.Icon
import arrow.core.Option
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.IconPacksRepository

class GetIconForApp(
    private val getSystemIconForApp: GetSystemIconForApp,
    private val iconPacksRepository: IconPacksRepository,
) {

    suspend operator fun invoke(id: AppId, iconPackId: Option<AppId>): Icon {
        val systemIcon = getSystemIconForApp(id)
        return iconPackId.fold(
            ifEmpty = { systemIcon },
            ifSome = { iconPacksRepository.getIcon(iconPackId = it, appId = id, defaultIcon = systemIcon) }
        )
    }
}
