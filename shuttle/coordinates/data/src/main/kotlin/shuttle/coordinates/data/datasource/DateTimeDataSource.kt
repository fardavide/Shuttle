package shuttle.coordinates.data.datasource

import com.soywiz.klock.DateTime
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.time.Duration

class DateTimeDataSource(
    private val refreshInterval: Duration
) {

    val flow: Flow<DateTime> = flow {
        while (currentCoroutineContext().isActive) {
            emit(DateTime.now())
            delay(refreshInterval)
        }
    }
}
