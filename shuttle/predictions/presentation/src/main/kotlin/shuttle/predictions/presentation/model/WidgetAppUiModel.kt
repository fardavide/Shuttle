package shuttle.predictions.presentation.model

import android.content.Intent
import android.graphics.drawable.Drawable
import shuttle.apps.domain.model.AppId

data class WidgetAppUiModel(
    val id: AppId,
    val name: String,
    val icon: Drawable,
    val launchIntent: Intent
)
