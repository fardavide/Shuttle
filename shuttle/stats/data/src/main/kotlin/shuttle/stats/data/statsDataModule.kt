package shuttle.stats.data

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import shuttle.stats.data.usecase.GetSortingRatios
import shuttle.stats.data.usecase.SortAppStatsByCounts
import shuttle.stats.domain.StatsRepository

val statsDataModule = module {

    factory { GetSortingRatios(getPrioritizeLocation = get()) }
    factory { SortAppStatsByCounts(computationDispatcher = Dispatchers.Default, getSortingRatios = get()) }
    factory<StatsRepository> {
        StatsRepositoryImpl(
            appsRepository = get(),
            statDataSource = get(),
            sortAppStatsByCounts = get()
        )
    }
}
