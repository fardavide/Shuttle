package shuttle.icons.data

import org.koin.dsl.module
import shuttle.icons.domain.IconPacksRepository

val iconsDataModule = module {

    factory<IconPacksRepository> { IconPacksRepositoryImpl() }
    single { IconPackManager(appContext = get(), packageManager = get(), appsRepository = get()) }
}
