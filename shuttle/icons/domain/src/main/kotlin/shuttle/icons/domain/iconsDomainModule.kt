package shuttle.icons.domain

import org.koin.dsl.module
import shuttle.icons.domain.usecase.ObserveInstalledIconPacks

val iconsDomainModule = module {

    factory { ObserveInstalledIconPacks(appsRepository = get()) }
}
