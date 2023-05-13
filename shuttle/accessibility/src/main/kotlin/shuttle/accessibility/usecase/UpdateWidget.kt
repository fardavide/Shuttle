package shuttle.accessibility.usecase

import android.content.Context
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import shuttle.widget.ui.SuggestedAppsWidget

class UpdateWidget(
    private val appContext: Context,
    private val appScope: CoroutineScope
) {

    operator fun invoke() {
        appScope.launch {
            SuggestedAppsWidget().updateAll(appContext)
        }
    }
}
