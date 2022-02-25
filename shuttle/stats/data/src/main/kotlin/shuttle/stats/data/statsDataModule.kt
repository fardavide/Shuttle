package shuttle.stats.data

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import shuttle.stats.domain.StatsRepository

val statsDataModule = module {

    factory { SortAppStatsByCounts(computationDispatcher = Dispatchers.Default) }
    factory<StatsRepository> {
        StatsRepositoryImpl(
            appsRepository = get(),
            statDataSource = get(),
            sortAppStatsByCounts = get()
        )
    }
}
