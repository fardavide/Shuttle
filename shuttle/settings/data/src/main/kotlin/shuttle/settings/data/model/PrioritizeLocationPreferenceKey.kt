package shuttle.settings.data.model

import androidx.datastore.preferences.core.booleanPreferencesKey

internal val PrioritizeLocationPreferenceKey = booleanPreferencesKey("prioritize location")

@Deprecated(
    "Use PrioritizeLocationPreferenceKey",
    ReplaceWith(
        "PrioritizeLocationPreferenceKey",
        "shuttle.settings.data.model.PrioritizeLocationPreferenceKey"
    )
)
internal val UseCurrentLocationOnlyPreferenceKey = booleanPreferencesKey("use current location only")
