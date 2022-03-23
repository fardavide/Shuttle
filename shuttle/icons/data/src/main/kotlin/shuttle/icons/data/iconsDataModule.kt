package shuttle.icons.data

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import shuttle.icons.domain.IconPacksRepository

val iconsDataModule = module {

    factory<IconPacksRepository> { IconPacksRepositoryImpl(packageManager = get(), dispatcher = Dispatchers.IO) }
}
