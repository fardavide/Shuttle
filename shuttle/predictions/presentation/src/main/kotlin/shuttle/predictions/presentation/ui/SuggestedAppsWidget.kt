package shuttle.predictions.presentation.ui

import android.content.Intent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
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
import shuttle.predictions.presentation.model.WidgetAppUiModel
import shuttle.predictions.presentation.model.WidgetSettingsUiModel
import shuttle.predictions.presentation.viewmodel.SuggestedAppsWidgetViewModel
import shuttle.predictions.presentation.viewmodel.SuggestedAppsWidgetViewModel.State

class SuggestedAppsWidget : GlanceAppWidget(), KoinComponent {

    private val viewModel: SuggestedAppsWidgetViewModel by inject()

    @Composable
    override fun Content() {
        Box(
            modifier = GlanceModifier
                .wrapContentSize()
                .cornerRadius(Dimens.Margin.Large)
        ) {
            when (val state = viewModel.state) {
                is State.Data -> SuggestedAppsList(data = state) { intent ->
                    actionStartActivity(intent)
                }
                is State.Error -> Box(modifier = GlanceModifier
                    .padding(Dimens.Margin.Small)
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.78f))
                ) {
                    Text(text = state.toString())
                }
            }
        }
    }

    @Composable
    private fun SuggestedAppsList(
        data: State.Data,
        onAppClick: (Intent) -> Action
    ) {
        val settings = data.widgetSettings
        val rows = settings.rowsCount
        val columns = settings.columnsCount
        val apps = data.apps.take(rows * columns).reversed()
        var index = 0

        Column(modifier = GlanceModifier
            .padding(horizontal = settings.horizontalSpacing, vertical = settings.verticalSpacing)
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.78f))
        ) {
            repeat(rows) {
                Row {
                    repeat(columns) {
                        AppIconItem(
                            app = apps[index++],
                            widgetSettings = settings,
                            onAppClick = onAppClick
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun AppIconItem(
        app: WidgetAppUiModel,
        widgetSettings: WidgetSettingsUiModel,
        onAppClick: (Intent) -> Action
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = GlanceModifier
                .padding(vertical = widgetSettings.verticalSpacing, horizontal = widgetSettings.horizontalSpacing)
                .clickable(onAppClick(app.launchIntent))
        ) {

            Image(
                provider = ImageProvider(app.icon),
                contentDescription = "",
                modifier = GlanceModifier.size(widgetSettings.iconSize)
            )
            Spacer(modifier = GlanceModifier.height(widgetSettings.verticalSpacing))
            Text(
                text = app.name,
                maxLines = 1,
                style = TextStyle(fontSize = widgetSettings.textSize, textAlign = TextAlign.Center),
                modifier = GlanceModifier.width(widgetSettings.iconSize)
            )
        }
    }
}

class SuggestedAppsWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget = SuggestedAppsWidget()
}
