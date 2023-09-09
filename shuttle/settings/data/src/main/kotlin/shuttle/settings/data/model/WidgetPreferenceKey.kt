package shuttle.settings.data.model

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

internal object WidgetPreferenceKey {

    val AllowTwoLines = booleanPreferencesKey("allow two lines")
    val ColumnsCount = intPreferencesKey("columns")
    val HorizontalSpacing = intPreferencesKey("horizontal spacing")
    val IconSize = intPreferencesKey("icon size")
    val RowsCount = intPreferencesKey("rows")
    val TextSize = intPreferencesKey("text size")
    val Transparency = intPreferencesKey("transparency")
    val UseMaterialColors = booleanPreferencesKey("use material colors")
    val VerticalSpacing = intPreferencesKey("vertical spacing")
}
