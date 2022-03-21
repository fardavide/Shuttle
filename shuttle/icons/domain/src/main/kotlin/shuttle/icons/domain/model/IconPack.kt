package shuttle.icons.domain.model

import shuttle.apps.domain.model.AppId

data class IconPack(
    val id: AppId,
    val drawables: Map<AppId, DrawableName>
) {

    @JvmInline
    value class DrawableName(val value: String)
}
