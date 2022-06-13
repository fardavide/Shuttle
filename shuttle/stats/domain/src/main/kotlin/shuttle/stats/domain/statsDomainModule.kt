package shuttle.stats.domain

import org.koin.dsl.module
import shuttle.stats.domain.usecase.StoreOpenStatsByCoordinates

val statsDomainModule = module {

    factory { StoreOpenStatsByCoordinates(statsRepository = get()) }

}
