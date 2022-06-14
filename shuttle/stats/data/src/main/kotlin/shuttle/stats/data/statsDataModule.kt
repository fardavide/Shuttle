package shuttle.stats.data

import kotlinx.coroutines.Dispatchers
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import shuttle.stats.data.mapper.DatabaseDateMapper
import shuttle.stats.data.usecase.MigrateStatsToSingleTable
import shuttle.stats.data.usecase.SortAppStats
import shuttle.stats.data.worker.MigrateStatsToSingleTableWorker
import shuttle.stats.domain.StatsRepository

val statsDataModule = module {

    factory { DatabaseDateMapper() }
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
            observeCurrentDateTime = get()
        )
    }
    factory<StatsRepository> {
        StatsRepositoryImpl(
            appsRepository = get(),
            databaseDateMapper = get(),
            migrateStatsToSingleTableScheduler = get(),
            sortAppStats = get(),
            statDataSource = get()
        )
    }
}
