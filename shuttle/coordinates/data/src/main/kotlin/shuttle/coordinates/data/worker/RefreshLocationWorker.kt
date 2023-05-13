package shuttle.coordinates.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import co.touchlab.kermit.Logger
import org.koin.android.annotation.KoinWorker
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import shuttle.coordinates.domain.CoordinatesQualifier
import shuttle.coordinates.domain.error.LocationError
import shuttle.coordinates.domain.repository.CoordinatesRepository
import kotlin.time.Duration

@KoinWorker
internal class RefreshLocationWorker(
    appContext: Context,
    params: WorkerParameters,
    private val coordinatesRepository: CoordinatesRepository,
    private val logger: Logger
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = coordinatesRepository.refreshLocation().fold(
        ifLeft = { locationError ->
            logger.w(locationError.toString())
            when (locationError) {
                LocationError.MissingPermissions -> Result.failure()
                LocationError.ExpiredLocation,
                LocationError.NoCachedLocation,
                LocationError.Timeout -> Result.retry()
            }
        },
        ifRight = { Result.success() }
    )

    companion object {

        const val Name = "RefreshLocationWorker"
    }
}

@Factory
class RefreshLocationScheduler(
    private val workManager: WorkManager,
    @Named(CoordinatesQualifier.Interval.Location.Scheduler.Default)
    private val every: Duration,
    @Named(CoordinatesQualifier.Interval.Location.Scheduler.Flex)
    private val flex: Duration
) {

    fun schedule() {
        val request = PeriodicWorkRequestBuilder<RefreshLocationWorker>(every.java(), flex.java()).build()
        workManager.enqueueUniquePeriodicWork(RefreshLocationWorker.Name, ExistingPeriodicWorkPolicy.UPDATE, request)
    }

    private fun Duration.java() = java.time.Duration.ofMinutes(inWholeMinutes)
}
