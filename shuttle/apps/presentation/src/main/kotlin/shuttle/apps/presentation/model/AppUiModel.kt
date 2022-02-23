package shuttle.apps.presentation.model

import android.graphics.drawable.Drawable
import shuttle.apps.domain.model.AppId

data class AppUiModel(
    val id: AppId,
    val name: String,
    val icon: Drawable
)
