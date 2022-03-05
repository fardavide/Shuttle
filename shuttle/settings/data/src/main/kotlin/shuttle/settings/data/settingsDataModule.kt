package shuttle.settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.dsl.module
import shuttle.settings.domain.SettingsRepository

val settingsDataModule = module {

    factory<SettingsRepository> {
        SettingsRepositoryImpl(dataStore = get<Context>().dataStore, settingDataSource = get())
    }
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
