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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import shuttle.design.Dimens
import shuttle.predictions.presentation.model.WidgetAppUiModel
import shuttle.predictions.presentation.resources.Strings
import shuttle.predictions.presentation.viewmodel.SuggestedAppsWidgetViewModel

class SuggestedAppsWidget : GlanceAppWidget(), KoinComponent {

    private val viewModel: SuggestedAppsWidgetViewModel by inject()

    @Composable
    override fun Content() {
        Column {
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
        val minCellSize = Dimens.Icon.Large + Dimens.Margin.Large
        LazyVerticalGrid(
            gridCells = GridCells.Adaptive(minCellSize),
            modifier = GlanceModifier
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.78f))
                .cornerRadius(Dimens.Margin.Large)
        ) {
            items(apps.take(25), itemId = { app -> app.id.hashCode().toLong() }) {
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
                .padding(Dimens.Margin.Small)
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
//                style = MaterialTheme.typography.titleMedium,
                modifier = GlanceModifier.width(width)
            )
        }
    }
}

class SuggestedAppsWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget = SuggestedAppsWidget()
}
