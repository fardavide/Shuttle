package shuttle.settings.data

import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.runBlocking
import org.koin.core.annotation.Factory
import shuttle.settings.data.model.PrioritizeLocationPreferenceKey
import shuttle.settings.data.model.UseCurrentLocationOnlyPreferenceKey

@Factory
internal class MigratePreferences(dataStoreProvider: DataStoreProvider) {

    private val dataStore = dataStoreProvider.dataStore()

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
