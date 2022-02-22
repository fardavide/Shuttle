package shuttle.apps.domain

import org.koin.dsl.module
import shuttle.apps.domain.usecase.GetAllInstalledApps

val appsDomainModule = module {

    factory { GetAllInstalledApps(repository = get()) }
}
