package shuttle.settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
class SettingsDataModule

fun interface DataStoreProvider {

    fun dataStore(): DataStore<Preferences>
}

@Single
class RealDataStoreProvider(context: Context) : DataStoreProvider {

    private val dataStore by lazy { context.dataStore }
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun dataStore() = dataStore
}

