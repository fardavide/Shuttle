package shuttle.icons.data

import org.koin.dsl.module

val iconsDataModule = module {

    single { IconPackManager(appContext = get(), packageManager = get()) }
}
