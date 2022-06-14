package shuttle.stats.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import shuttle.stats.data.usecase.MigrateStatsToSingleTable
import kotlin.time.Duration

internal class MigrateStatsToSingleTableWorker(
    appContext: Context,
    params: WorkerParameters,
    private val migrateStatsToSingleTable: MigrateStatsToSingleTable
): CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result =
        runCatching { migrateStatsToSingleTable() }
            .fold(
                onSuccess = { Result.success() },
                onFailure = { Result.failure() }
            )

    class Scheduler(private val workManager: WorkManager) {

        fun schedule() {
            val request = OneTimeWorkRequestBuilder<MigrateStatsToSingleTableWorker>().build()
            workManager.enqueueUniqueWork(Name, ExistingWorkPolicy.REPLACE, request)
        }

        private fun Duration.java() = java.time.Duration.ofMinutes(inWholeMinutes)
    }

    companion object {

        const val Name = "MigrateStatsToSingleTableWorker"
    }
}
