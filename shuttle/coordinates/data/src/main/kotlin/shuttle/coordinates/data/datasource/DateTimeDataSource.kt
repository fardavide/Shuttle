package shuttle.coordinates.data.datasource

import korlibs.time.DateTime
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import shuttle.coordinates.domain.CoordinatesQualifier
import kotlin.time.Duration

@Factory
class DateTimeDataSource(
    @Named(CoordinatesQualifier.Interval.Location.MinRefresh)
    private val refreshInterval: Duration
) {

    val flow: Flow<DateTime> = flow {
        while (currentCoroutineContext().isActive) {
            emit(DateTime.now())
            delay(refreshInterval)
        }
    }
}
