package shuttle.icons.domain.model

import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppName

data class IconPack(
    val id: AppId,
    val name: AppName,
    val drawables: Map<AppId, DrawableName>
) {

    @JvmInline
    value class DrawableName(val value: String)
}
