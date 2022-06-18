package shuttle.stats.data

import kotlinx.coroutines.Dispatchers
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import shuttle.stats.data.mapper.DatabaseDateMapper
import shuttle.stats.data.usecase.DeleteOldStats
import shuttle.stats.data.usecase.MigrateStatsToSingleTable
import shuttle.stats.data.usecase.SortAppStats
import shuttle.stats.data.worker.DeleteOldStatsWorker
import shuttle.stats.data.worker.MigrateStatsToSingleTableWorker
import shuttle.stats.domain.StatsRepository

val statsDataModule = module {

    factory { DatabaseDateMapper() }
    factory { DeleteOldStats(databaseDateMapper = get(), observeCurrentDateTime = get(), statDataSource = get()) }
    worker { DeleteOldStatsWorker(appContext = get(), params = get(), migrateStatsToSingleTable = get()) }
    factory { DeleteOldStatsWorker.Scheduler(workManager = get()) }
    factory {
        MigrateStatsToSingleTable(
            databaseDateMapper = get(),
            observeCurrentDateTime = get(),
            statDataSource = get()
        )
    }
    worker {
        MigrateStatsToSingleTableWorker(appContext = get(), params = get(), migrateStatsToSingleTable = get())
    }
    factory { MigrateStatsToSingleTableWorker.Scheduler(workManager = get()) }
    factory {
        SortAppStats(
            databaseDateMapper = get(),
            computationDispatcher = Dispatchers.Default,
            observeCurrentCoordinates = get()
        )
    }
    factory<StatsRepository> {
        StatsRepositoryImpl(
            appsRepository = get(),
            databaseDateMapper = get(),
            deleteOldStatsScheduler = get(),
            migrateStatsToSingleTableScheduler = get(),
            sortAppStats = get(),
            statDataSource = get()
        )
    }
}
