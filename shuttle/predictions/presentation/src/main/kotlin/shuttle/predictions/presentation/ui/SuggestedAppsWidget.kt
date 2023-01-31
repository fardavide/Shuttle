package shuttle.predictions.presentation.ui

import android.content.Intent
import androidx.compose.runtime.Composable
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
import shuttle.design.theme.Dimens
import shuttle.predictions.presentation.RefreshCurrentLocationActionCallback
import shuttle.predictions.presentation.model.SuggestedAppsState
import shuttle.predictions.presentation.model.WidgetAppUiModel
import shuttle.predictions.presentation.model.WidgetSettingsUiModel
import shuttle.predictions.presentation.viewmodel.SuggestedAppsWidgetViewModel
import shuttle.utils.kotlin.takeOrFillWithNulls

class SuggestedAppsWidget : GlanceAppWidget(), KoinComponent {

    private val viewModel: SuggestedAppsWidgetViewModel by inject()

    @Composable
    override fun Content() {
        val actions = Actions(
            onOpenApp = ::actionStartActivity,
            onRefreshLocation = actionRunCallback<RefreshCurrentLocationActionCallback>()
        )

        Box(
            modifier = GlanceModifier
                .wrapContentSize()
                .cornerRadius(Dimens.Margin.Large)
        ) {
            when (val state = viewModel.state) {
                is SuggestedAppsState.Data -> WidgetContent(data = state, actions)
                is SuggestedAppsState.Error -> Box(
                    modifier = GlanceModifier
                        .padding(Dimens.Margin.Small)
                        .widgetBackground()
                ) {
                    Text(text = state.toString())
                }
            }
        }
    }
    
    @Composable
    private fun WidgetContent(
        data: SuggestedAppsState.Data,
        actions: Actions
    ) {
        val settings = data.widgetSettings

        Column(
            modifier = GlanceModifier
                .padding(horizontal = settings.horizontalSpacing, vertical = settings.verticalSpacing)
                .widgetBackground()
        ) {
            SuggestedAppsList(data = data, actions = actions)
        }
    }

    @Composable
    private fun SuggestedAppsList(
        data: SuggestedAppsState.Data,
        actions: Actions
    ) {
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
        ) {

            Image(
                provider = ImageProvider(app.icon),
                contentDescription = "",
                modifier = GlanceModifier.size(widgetSettings.iconSize)
            )
            Spacer(modifier = GlanceModifier.height(widgetSettings.verticalSpacing))
            Text(
                text = app.name,
                maxLines = widgetSettings.maxLines,
                style = TextStyle(fontSize = widgetSettings.textSize, textAlign = TextAlign.Center),
                modifier = GlanceModifier.width(widgetSettings.iconSize)
            )
        }
    }
    
    @Composable
    private fun GlanceModifier.widgetBackground(): GlanceModifier {
        val context = LocalContext.current
        return background(
            day = DynamicThemeColorProviders.background.getColor(context).copy(alpha = 0.78f),
            night = DynamicThemeColorProviders.background.getColor(context).copy(alpha = 0.64f)
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
