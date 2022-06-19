package shuttle.settings.data.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

fun mockDataStore(): DataStore<Preferences> = mockk {
    val map = mutableMapOf<String, Any>()
    val preferences: MutablePreferences = mockk {
        every { toMutablePreferences() } returns this
        every { get(key = any<Preferences.Key<Any>>()) } answers {
            val name = firstArg<Preferences.Key<Any>>().name
            map[name]
        }
        every { set(key = any(), value = any<Any>()) } answers {
            val name = firstArg<Preferences.Key<Any>>().name
            map[name] = secondArg()
        }
    }
    val preferencesFlow = MutableStateFlow(preferences)
    every { data } returns preferencesFlow
    coEvery { updateData(transform = any()) } coAnswers  {
        val transform = firstArg<suspend (Preferences) -> Preferences>()
        transform(preferences)
        preferencesFlow.emit(preferences)
        preferences
    }
}
