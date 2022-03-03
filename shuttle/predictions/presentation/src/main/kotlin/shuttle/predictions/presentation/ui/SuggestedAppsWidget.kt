package shuttle.predictions.presentation.ui

import android.content.Intent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.GridCells
import androidx.glance.appwidget.lazy.LazyVerticalGrid
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import shuttle.design.theme.Dimens
import shuttle.predictions.presentation.model.WidgetAppUiModel
import shuttle.predictions.presentation.resources.Strings
import shuttle.predictions.presentation.viewmodel.SuggestedAppsWidgetViewModel

class SuggestedAppsWidget : GlanceAppWidget(), KoinComponent {

    private val viewModel: SuggestedAppsWidgetViewModel by inject()

    @Composable
    override fun Content() {
        Column(
            modifier = GlanceModifier
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.78f))
                .cornerRadius(Dimens.Margin.Large)
        ) {
            when (val state = viewModel.state) {
                is SuggestedAppsWidgetViewModel.State.Data -> SuggestedAppsList(apps = state.apps) { intent ->
                    actionStartActivity(intent)
                }
                is SuggestedAppsWidgetViewModel.State.Error -> Text(text = state.toString())
            }
        }
    }

    @Composable
    private fun SuggestedAppsList(
        apps: List<WidgetAppUiModel>,
        onAppClick: (Intent) -> Action
    ) {
        LazyVerticalGrid(gridCells = GridCells.Fixed(count = 5)) {
            items(apps.take(10).reversed(), itemId = { app -> app.id.hashCode().toLong() }) {
                AppIconItem(it, onAppClick)
            }
        }
    }

    @Composable
    private fun AppIconItem(
        app: WidgetAppUiModel,
        onAppClick: (Intent) -> Action
    ) {
        val width = Dimens.Icon.Large
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = GlanceModifier
                .padding(vertical = Dimens.Margin.XSmall)
                .fillMaxWidth()
                .clickable(onAppClick(app.launchIntent))
        ) {

            Image(
                provider = ImageProvider(app.icon),
                contentDescription = Strings.AppIconContentDescription,
                modifier = GlanceModifier.size(width)
            )
            Spacer(modifier = GlanceModifier.height(Dimens.Margin.Small))
            Text(
                text = app.name,
                maxLines = 1,
                style = TextStyle(fontSize = 12.sp, textAlign = TextAlign.Center),
                modifier = GlanceModifier.width(width)
            )
        }
    }
}

class SuggestedAppsWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget = SuggestedAppsWidget()
}
