package shuttle.icons.domain

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import shuttle.icons.domain.usecase.GetIconBitmapForApp
import shuttle.icons.domain.usecase.GetIconDrawableForApp
import shuttle.icons.domain.usecase.GetSystemIconBitmapForApp
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp
import shuttle.icons.domain.usecase.ObserveInstalledIconPacks

val iconsDomainModule = module {

    factory { GetIconBitmapForApp(getSystemIconBitmapForApp = get(), iconsPacksRepository = get()) }
    factory { GetIconDrawableForApp(getSystemIconForApp = get(), iconsPacksRepository = get()) }
    factory { GetSystemIconBitmapForApp(getSystemIconDrawableForApp = get(), ioDispatcher = Dispatchers.IO) }
    factory { GetSystemIconDrawableForApp(packageManager = get(), ioDispatcher = Dispatchers.IO) }
    factory { ObserveInstalledIconPacks(appsRepository = get()) }
}
