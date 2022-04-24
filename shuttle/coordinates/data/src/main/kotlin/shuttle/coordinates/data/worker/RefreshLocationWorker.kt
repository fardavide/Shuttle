package shuttle.coordinates.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import co.touchlab.kermit.Logger
import shuttle.coordinates.domain.CoordinatesRepository
import shuttle.coordinates.domain.error.LocationError
import kotlin.time.Duration

internal class RefreshLocationWorker(
    appContext: Context,
    params: WorkerParameters,
    private val coordinatesRepository: CoordinatesRepository,
    private val logger: Logger
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result =
        coordinatesRepository.refreshLocation().fold(
            ifLeft = { locationError ->
                logger.w(locationError.toString())
                when (locationError) {
                    LocationError.MissingPermissions -> Result.failure()
                    LocationError.ExpiredLocation, LocationError.NoCachedLocation -> Result.retry()
                }
            },
            ifRight = { Result.success() }
        )

    class Scheduler(
        private val workManager: WorkManager,
        private val every: Duration,
        private val flex: Duration
    ) {

        fun schedule() {
            val request = PeriodicWorkRequestBuilder<RefreshLocationWorker>(every.java(), flex.java()).build()
            workManager.enqueueUniquePeriodicWork(Name, ExistingPeriodicWorkPolicy.REPLACE, request)
        }

        private fun Duration.java() = java.time.Duration.ofMinutes(inWholeMinutes)
    }

    companion object {

        const val Name = "RefreshLocationWorker"
    }
}
