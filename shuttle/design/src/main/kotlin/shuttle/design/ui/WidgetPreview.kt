package shuttle.design.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import shuttle.design.PreviewUtils
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.design.model.WidgetPreviewAppUiModel
import shuttle.design.model.WidgetPreviewUiModel
import shuttle.design.theme.Dimens
import shuttle.resources.R.string
import shuttle.utils.kotlin.takeOrFillWithNulls

@Composable
fun WidgetPreview(model: WidgetPreviewUiModel) {
    val layout = model.layout

    val rows = layout.rowsCount
    val columns = layout.columnsCount
    val apps = remember { model.apps.takeOrFillWithNulls(rows * columns).reversed() }
    var index = 0

    val color = run {
        val baseColor = if (model.layout.useMaterialColors) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        }
        baseColor.copy(alpha = model.layout.transparency * 0.01f)
    }
    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(
                color = color,
                shape = RoundedCornerShape(Dimens.Margin.Large)
            )
            .padding(horizontal = layout.horizontalSpacing, vertical = layout.verticalSpacing)
    ) {
        repeat(rows) {
            Row {
                repeat(columns) {
                    AppIconItem(
                        app = apps.getOrNull(index++),
                        widgetSettings = layout
                    )
                }
            }
        }
    }
}

@Composable
private fun AppIconItem(app: WidgetPreviewAppUiModel?, widgetSettings: WidgetLayoutUiModel) {
    app ?: return

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            vertical = widgetSettings.verticalSpacing,
            horizontal = widgetSettings.horizontalSpacing
        )
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = app.icon),
            contentDescription = stringResource(id = string.x_app_icon_description),
            modifier = Modifier.size(widgetSettings.iconSize)
        )
        Spacer(modifier = Modifier.height(widgetSettings.verticalSpacing))
        val textColor = if (widgetSettings.useMaterialColors) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        }
        Text(
            modifier = Modifier.width(widgetSettings.iconSize),
            text = app.name,
            color = textColor,
            maxLines = if (widgetSettings.allowTwoLines) 2 else 1,
            fontSize = widgetSettings.textSize,
            lineHeight = widgetSettings.textSize,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 1_234_567,
    widthDp = PreviewUtils.Dimens.Medium.Width,
    heightDp = PreviewUtils.Dimens.Medium.Height
)
private fun WidgetPreviewPreview() {
    val icon = PreviewUtils.ShuttleIconDrawable
    val apps = listOf(
        WidgetPreviewAppUiModel("Shuttle", icon),
        WidgetPreviewAppUiModel("Proton Mail", icon),
        WidgetPreviewAppUiModel("Proton Drive", icon),
        WidgetPreviewAppUiModel("Telegram", icon),
        WidgetPreviewAppUiModel("Facebook", icon),
        WidgetPreviewAppUiModel("Instagram", icon)
    )
    val widgetSettings = WidgetLayoutUiModel(
        allowTwoLines = true,
        columnsCount = 3,
        horizontalSpacing = 10.dp,
        iconSize = 48.dp,
        rowsCount = 2,
        textSize = 9.sp,
        transparency = 70,
        useMaterialColors = true,
        verticalSpacing = 10.dp
    )
    MaterialTheme {
        WidgetPreview(WidgetPreviewUiModel(apps = apps, layout = widgetSettings))
    }
}
