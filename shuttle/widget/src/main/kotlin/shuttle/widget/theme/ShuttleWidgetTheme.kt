package shuttle.widget.theme

import androidx.compose.runtime.Composable
import androidx.glance.GlanceTheme

@Composable
internal fun ShuttleWidgetTheme(content: @Composable () -> Unit) {
    GlanceTheme(content = content)
}
