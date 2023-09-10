package shuttle.accessibility.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import shuttle.coordinates.domain.usecase.RefreshLocation

@Factory
internal class StartRefreshLocationAndUpdateWidget(
    private val appScope: CoroutineScope,
    private val refreshLocation: RefreshLocation,
    private val updateWidget: UpdateWidget
) {

    operator fun invoke() {
        appScope.launch {
            refreshLocation()
            updateWidget()
        }
    }
}
