package shuttle.accessibility.usecase

import android.content.Context
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import shuttle.widget.ui.SuggestedAppsWidget

@Single
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
