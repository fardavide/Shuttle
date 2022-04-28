package shuttle.settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.core.scope.Scope
import org.koin.dsl.module
import shuttle.settings.domain.SettingsRepository

val settingsDataModule = module {

    factory { MigratePreferences(dataStore = dataStore) }
    factory<SettingsRepository> { SettingsRepositoryImpl(dataStore = dataStore, settingDataSource = get()) }
}

val Scope.dataStore get() = get<Context>().dataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
