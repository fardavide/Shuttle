package shuttle.stats.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import shuttle.stats.data.usecase.DeleteOldStats
import kotlin.time.Duration

internal class DeleteOldStatsWorker(
    appContext: Context,
    params: WorkerParameters,
    private val deleteOldStats: DeleteOldStats
): CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result =
        runCatching { deleteOldStats() }
            .fold(
                onSuccess = { Result.success() },
                onFailure = { Result.failure() }
            )

    class Scheduler(
        private val workManager: WorkManager,
        private val repeatInterval: Duration,
        private val flexInterval: Duration
    ) {

        fun schedule() {
            val request = PeriodicWorkRequestBuilder<DeleteOldStatsWorker>(
                repeatInterval = repeatInterval.java(),
                flexTimeInterval = flexInterval.java(),
            ).build()
            workManager.enqueueUniquePeriodicWork(Name, ExistingPeriodicWorkPolicy.KEEP, request)
        }

        private fun Duration.java() = java.time.Duration.ofMinutes(inWholeMinutes)
    }

    companion object {

        const val Name = "DeleteOldStatsWorker"
    }
}
