package shuttle.settings.data.model

import androidx.datastore.preferences.core.intPreferencesKey

internal object WidgetSettingsPreferenceKeys {

    val RowsCount = intPreferencesKey("rows")
    val ColumnsCount = intPreferencesKey("columns")
    val IconSize = intPreferencesKey("icon size")
    val Spacing = intPreferencesKey("spacing")
    val TextSize = intPreferencesKey("text size")
}
