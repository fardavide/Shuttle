package shuttle.stats.data.worker

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
import shuttle.stats.data.usecase.DeleteOldStats
import shuttle.stats.domain.StatsQualifier
import kotlin.time.Duration

@KoinWorker
internal class DeleteOldStatsWorker(
    appContext: Context,
    params: WorkerParameters,
    private val deleteOldStats: DeleteOldStats
) : CoroutineWorker(appContext, params) {

    private val logger = Logger.withTag("DeleteOldStatsWorker")

    override suspend fun doWork(): Result = runCatching { deleteOldStats() }
        .fold(
            onSuccess = {
                logger.i("Successfully deleted old stats")
                Result.success()
            },
            onFailure = {
                logger.e(it) { "Failed to delete old stats" }
                Result.failure()
            }
        )

    companion object {

        const val Name = "DeleteOldStatsWorker"
    }
}

@Factory
class DeleteOldStatsScheduler(
    private val workManager: WorkManager,
    @Named(StatsQualifier.Interval.DeleteOldStats.Scheduler.Default)
    private val repeatInterval: Duration,
    @Named(StatsQualifier.Interval.DeleteOldStats.Scheduler.Flex)
    private val flexInterval: Duration
) {

    fun schedule() {
        val request = PeriodicWorkRequestBuilder<DeleteOldStatsWorker>(
            repeatInterval = repeatInterval.java(),
            flexTimeInterval = flexInterval.java()
        ).build()
        workManager.enqueueUniquePeriodicWork(DeleteOldStatsWorker.Name, ExistingPeriodicWorkPolicy.KEEP, request)
    }

    private fun Duration.java() = java.time.Duration.ofMinutes(inWholeMinutes)
}
