package shuttle.settings.presentation.model

import android.graphics.drawable.Drawable
import shuttle.apps.domain.model.AppId
import shuttle.design.StringResource
import shuttle.settings.presentation.resources.Strings

internal sealed interface IconPackSettingsUiModel {

    val isSelected: Boolean

    data class FromApp(
        val id: AppId,
        val name: String,
        val icon: Drawable,
        override val isSelected: Boolean
    ) : IconPackSettingsUiModel

    data class SystemDefault(
        val name: StringResource<Strings.IconPack>,
        override val isSelected: Boolean
    ) : IconPackSettingsUiModel
}
