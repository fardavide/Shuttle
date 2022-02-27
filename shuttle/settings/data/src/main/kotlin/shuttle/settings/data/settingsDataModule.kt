package shuttle.settings.data

import org.koin.dsl.module
import shuttle.settings.domain.SettingsRepository

val settingsDataModule = module {

    factory<SettingsRepository> { SettingsRepositoryImpl() }
}
