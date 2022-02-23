package shuttle.stats.domain

import org.koin.dsl.module
import shuttle.stats.domain.usecase.GetAllAppsStats
import shuttle.stats.domain.usecase.IncrementOpenCounter
import shuttle.stats.domain.usecase.LocationToLocationRange
import shuttle.stats.domain.usecase.MetersToLatitude
import shuttle.stats.domain.usecase.MetersToLongitude
import shuttle.stats.domain.usecase.ObserveMostUsedApps

val statsDomainModule = module {

    factory { IncrementOpenCounter(statsRepository = get()) }
    factory { LocationToLocationRange(metersToLatitude = get(), metersToLongitude = get()) }
    factory { MetersToLatitude() }
    factory { MetersToLongitude() }
    factory { ObserveMostUsedApps(statsRepository = get(), locationToLocationRange = get()) }
    factory { GetAllAppsStats() }

}
