package shuttle.stats.domain

import org.koin.dsl.module
import shuttle.stats.domain.usecase.IncrementOpenCounterByCoordinates

val statsDomainModule = module {

    factory { IncrementOpenCounterByCoordinates(statsRepository = get()) }

}
