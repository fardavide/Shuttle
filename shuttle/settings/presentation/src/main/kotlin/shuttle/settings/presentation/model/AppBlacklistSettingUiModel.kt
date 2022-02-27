package shuttle.settings.presentation.model

import android.graphics.drawable.Drawable
import shuttle.apps.domain.model.AppId

data class AppBlacklistSettingUiModel(
    val id: AppId,
    val name: String,
    val icon: Drawable,
    val isBlacklisted: Boolean
)
