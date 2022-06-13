package shuttle.stats.data

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import shuttle.stats.data.mapper.DatabaseDateMapper
import shuttle.stats.data.usecase.SortAppStats
import shuttle.stats.domain.StatsRepository

val statsDataModule = module {

    factory { DatabaseDateMapper() }
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
            statDataSource = get(),
            sortAppStats = get()
        )
    }
}
