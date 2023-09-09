package shuttle.settings.data.model

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

internal object AppPreferenceKey {

    val CurrentIconPack = stringPreferencesKey("current icon pack")
    val DidShowOnboarding = booleanPreferencesKey("did show onboarding")
    val HasAccessibilityService = booleanPreferencesKey("has enabled accessibility service")
    val KeepStatisticsFor = intPreferencesKey("keep_statistics_for_months")
    val UseExperimentalAppSorting = booleanPreferencesKey("use experimental app sorting")
}
