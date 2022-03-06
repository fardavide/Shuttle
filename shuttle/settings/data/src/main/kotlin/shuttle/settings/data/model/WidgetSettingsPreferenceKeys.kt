package shuttle.settings.data.model

import androidx.datastore.preferences.core.intPreferencesKey

internal object WidgetSettingsPreferenceKeys {

    val RowsCount = intPreferencesKey("rows")
    val ColumnsCount = intPreferencesKey("columns")
    val IconSize = intPreferencesKey("icon size")
    val HorizontalSpacing = intPreferencesKey("horizontal spacing")
    val VerticalSpacing = intPreferencesKey("vertical spacing")
    val TextSize = intPreferencesKey("text size")
}
