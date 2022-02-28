package shuttle.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.core.qualifier.named
import org.koin.dsl.module

val preferencesModule = module {

    factory(PreferencesIdentifier) {
        get<Context>().dataStore
    }
}

val PreferencesIdentifier = named("preferences")
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")
