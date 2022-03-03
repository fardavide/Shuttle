package shuttle.coordinates.data.datasource

import com.soywiz.klock.DateTime
import com.soywiz.klock.Time
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.time.Duration

internal class TimeDataSource(
    private val refreshInterval: Duration
) {

    val timeFlow: Flow<Time> = flow {
        while (currentCoroutineContext().isActive) {
            val time = DateTime.now().time
            emit(time)
            delay(refreshInterval)
        }
    }
}
