package shuttle.icons.domain

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import shuttle.icons.domain.usecase.GetIconDrawableForApp
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp
import shuttle.icons.domain.usecase.GetSystemIconForApp
import shuttle.icons.domain.usecase.ObserveInstalledIconPacks

val iconsDomainModule = module {

    factory { GetIconDrawableForApp(getSystemIconForApp = get(), iconsPacksRepository = get()) }
    factory { GetSystemIconDrawableForApp(packageManager = get(), ioDispatcher = Dispatchers.IO) }
    factory { GetSystemIconForApp(packageManager = get(), ioDispatcher = Dispatchers.IO) }
    factory { ObserveInstalledIconPacks(appsRepository = get()) }
}
