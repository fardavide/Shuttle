package shuttle.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.runBlocking
import shuttle.settings.data.model.PrioritizeLocationPreferenceKey
import shuttle.settings.data.model.UseCurrentLocationOnlyPreferenceKey

internal class MigratePreferences(private val dataStore: DataStore<Preferences>) {

    @Suppress("DEPRECATION") // Deprecated Keys
    operator fun invoke() {
        runBlocking {

            dataStore.edit { prefs ->
                prefs[UseCurrentLocationOnlyPreferenceKey]?.let {
                    prefs[PrioritizeLocationPreferenceKey] = it
                    prefs -= UseCurrentLocationOnlyPreferenceKey
                }
            }
        }
    }
}
