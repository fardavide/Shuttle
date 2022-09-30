package shuttle.predictions.presentation

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import shuttle.coordinates.domain.usecase.RefreshLocation

class RefreshCurrentLocationActionCallback : ActionCallback, KoinComponent {

    private val refreshLocation: RefreshLocation by inject()

    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        refreshLocation()
    }
}
