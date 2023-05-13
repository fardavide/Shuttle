package shuttle.widget.ui

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.color.DynamicThemeColorProviders
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentSize
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import shuttle.resources.NoContentDescription
import shuttle.utils.kotlin.takeOrFillWithNulls
import shuttle.widget.action.RefreshCurrentLocationActionCallback
import shuttle.widget.model.WidgetAppUiModel
import shuttle.widget.model.WidgetSettingsUiModel
import shuttle.widget.state.SuggestedAppsState
import shuttle.widget.theme.ShuttleWidgetTheme
import shuttle.widget.theme.WidgetDimen
import shuttle.widget.viewmodel.SuggestedAppsWidgetViewModel

class SuggestedAppsWidget : GlanceAppWidget(), KoinComponent {

    private val viewModel: SuggestedAppsWidgetViewModel by inject()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state by viewModel.state.collectAsState()
            ShuttleWidgetTheme {
                Content(state)
            }
        }
    }

    @Composable
    private fun Content(state: SuggestedAppsState) {
        val actions = Actions(
            onOpenApp = ::actionStartActivity,
            onRefreshLocation = actionRunCallback<RefreshCurrentLocationActionCallback>()
        )

        Box(
            modifier = GlanceModifier
                .wrapContentSize()
                .cornerRadius(WidgetDimen.Margin.Outer)
        ) {
            when (state) {
                is SuggestedAppsState.Data -> WidgetContent(data = state, actions)
                is SuggestedAppsState.Error -> Box(
                    modifier = GlanceModifier
                        .padding(WidgetDimen.Margin.Inner)
                        .widgetBackground()
                ) {
                    Text(text = state.toString())
                }
                is SuggestedAppsState.Loading -> Box(
                    modifier = GlanceModifier
                        .padding(WidgetDimen.Margin.Inner)
                        .widgetBackground()
                ) {
                    Text(text = "Loading...")
                }
            }
        }
    }

    @Composable
    private fun WidgetContent(data: SuggestedAppsState.Data, actions: Actions) {
        val settings = data.widgetSettings

        Column(
            modifier = GlanceModifier
                .padding(horizontal = settings.horizontalSpacing, vertical = settings.verticalSpacing)
                .widgetBackground(
                    transparency = data.widgetSettings.transparency,
                    useMaterialColors = data.widgetSettings.useMaterialColors
                )
        ) {
            SuggestedAppsList(data = data, actions = actions)
        }
    }

    @Composable
    private fun SuggestedAppsList(data: SuggestedAppsState.Data, actions: Actions) {
        val settings = data.widgetSettings
        val rows = settings.rowsCount
        val columns = settings.columnsCount
        val apps = data.apps.takeOrFillWithNulls(rows * columns).reversed()
        var index = 0

        repeat(rows) {
            Row {
                repeat(columns) {
                    AppIconItem(
                        app = apps[index++],
                        widgetSettings = settings,
                        actions = actions
                    )
                }
            }
        }
    }

    @Composable
    private fun AppIconItem(
        app: WidgetAppUiModel?,
        widgetSettings: WidgetSettingsUiModel,
        actions: Actions
    ) {
        app ?: return

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = GlanceModifier
                .padding(vertical = widgetSettings.verticalSpacing, horizontal = widgetSettings.horizontalSpacing)
                .clickable(actions.onOpenApp(app.launchIntent))
                .cornerRadius(WidgetDimen.CornerRadius)
        ) {
            Image(
                provider = ImageProvider(app.icon),
                contentDescription = NoContentDescription,
                modifier = GlanceModifier.size(widgetSettings.iconSize)
            )
            Spacer(modifier = GlanceModifier.height(widgetSettings.verticalSpacing))
            val textColor = if (widgetSettings.useMaterialColors) {
                DynamicThemeColorProviders.onPrimaryContainer
            } else {
                DynamicThemeColorProviders.onBackground
            }
            Text(
                modifier = GlanceModifier.width(widgetSettings.iconSize),
                text = app.name,
                maxLines = widgetSettings.maxLines,
                style = TextStyle(
                    color = textColor,
                    fontSize = widgetSettings.textSize,
                    textAlign = TextAlign.Center
                )
            )
        }
    }

    @Suppress("ModifierComposable")
    @Composable
    private fun GlanceModifier.widgetBackground(
        transparency: Float = .7f,
        useMaterialColors: Boolean = true
    ): GlanceModifier {
        val color = run {
            val context = LocalContext.current
            val baseColor = if (useMaterialColors) {
                DynamicThemeColorProviders.primaryContainer
            } else {
                DynamicThemeColorProviders.background
            }
            baseColor.getColor(context).copy(alpha = transparency)
        }
        return background(
            day = color,
            night = color
        )
    }

    data class Actions(
        val onOpenApp: (Intent) -> Action,
        val onRefreshLocation: Action
    )
}

class SuggestedAppsWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget = SuggestedAppsWidget()
}