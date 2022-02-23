package shuttle.stats.domain

import org.koin.dsl.module
import shuttle.stats.domain.usecase.IncrementOpenCounter

val statsDomainModule = module {

    factory { IncrementOpenCounter(statsRepository = get()) }

}
