package shuttle.icons.domain

import org.koin.dsl.module
import shuttle.icons.domain.usecase.GetSystemIconForApp
import shuttle.icons.domain.usecase.ObserveInstalledIconPacks

val iconsDomainModule = module {

    factory { GetSystemIconForApp(packageManager = get()) }
    factory { ObserveInstalledIconPacks(appsRepository = get()) }
}
