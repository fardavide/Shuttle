package shuttle.predictions.presentation.model

import android.content.Intent
import android.graphics.drawable.Icon
import shuttle.apps.domain.model.AppId

data class WidgetAppUiModel(
    val id: AppId,
    val name: String,
    val icon: Icon,
    val launchIntent: Intent
)
