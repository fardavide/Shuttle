package shuttle.settings.presentation.model

import android.graphics.drawable.Drawable
import arrow.core.None
import arrow.core.Option
import shuttle.apps.domain.model.AppId
import shuttle.design.StringResource
import shuttle.settings.presentation.resources.Strings

internal sealed interface IconPackSettingsItemUiModel {

    val isSelected: Boolean

    fun idOrNone(): Option<AppId> = when (this) {
        is FromApp -> Option(id)
        is SystemDefault -> None
    }

    data class FromApp(
        val id: AppId,
        val name: String,
        val icon: Drawable,
        override val isSelected: Boolean
    ) : IconPackSettingsItemUiModel

    data class SystemDefault(
        val name: StringResource<Strings.IconPack>,
        override val isSelected: Boolean
    ) : IconPackSettingsItemUiModel
}
