package shuttle.apps.domain

import org.koin.dsl.module
import shuttle.apps.domain.usecase.ObserveAllInstalledApps

val appsDomainModule = module {

    factory { ObserveAllInstalledApps(repository = get()) }
}
