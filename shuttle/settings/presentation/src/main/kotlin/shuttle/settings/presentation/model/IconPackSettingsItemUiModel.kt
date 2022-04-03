package shuttle.settings.presentation.model

import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import arrow.core.None
import arrow.core.Option
import shuttle.apps.domain.model.AppId

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
        @StringRes val name: Int,
        override val isSelected: Boolean
    ) : IconPackSettingsItemUiModel
}
