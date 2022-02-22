package shuttle.stats.domain

import org.koin.dsl.module
import shuttle.stats.domain.usecase.GetAllAppsStats

val statsDomainModule = module {

    factory { GetAllAppsStats() }

}
