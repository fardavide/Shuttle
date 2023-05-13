package shuttle.widget.model

import android.content.Intent
import android.graphics.Bitmap
import shuttle.apps.domain.model.AppId

internal data class WidgetAppUiModel(
    val id: AppId,
    val name: String,
    val icon: Bitmap,
    val launchIntent: Intent
)
