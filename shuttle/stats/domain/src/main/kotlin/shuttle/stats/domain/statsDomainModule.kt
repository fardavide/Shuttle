package shuttle.stats.domain

import org.koin.dsl.module
import shuttle.stats.domain.usecase.StartDeleteOldStats
import shuttle.stats.domain.usecase.StoreOpenStatsByCoordinates

val statsDomainModule = module {

    factory { StartDeleteOldStats(statsRepository = get()) }
    factory { StoreOpenStatsByCoordinates(statsRepository = get()) }
}
