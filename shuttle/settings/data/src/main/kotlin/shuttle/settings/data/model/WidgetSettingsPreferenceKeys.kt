package shuttle.settings.data.model

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

internal object WidgetSettingsPreferenceKeys {

    val AllowTwoLines = booleanPreferencesKey("allow two lines")
    val ColumnsCount = intPreferencesKey("columns")
    val HorizontalSpacing = intPreferencesKey("horizontal spacing")
    val IconSize = intPreferencesKey("icon size")
    val RowsCount = intPreferencesKey("rows")
    val ShowRefreshLocation = booleanPreferencesKey("show refresh location")
    val TextSize = intPreferencesKey("text size")
    val VerticalSpacing = intPreferencesKey("vertical spacing")
}
