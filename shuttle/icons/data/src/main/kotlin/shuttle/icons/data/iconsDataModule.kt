package shuttle.icons.data

import org.koin.dsl.module
import shuttle.icons.domain.IconsRepository

val iconsDataModule = module {

    factory<IconsRepository> { IconsRepositoryImpl() }
}
