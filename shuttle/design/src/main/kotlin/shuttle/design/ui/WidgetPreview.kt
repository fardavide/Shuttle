package shuttle.design.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import shuttle.design.PreviewUtils
import shuttle.design.model.WidgetLayoutUiModel
import shuttle.design.model.WidgetPreviewAppUiModel
import shuttle.design.model.WidgetPreviewUiModel
import shuttle.design.theme.Dimens
import shuttle.utils.kotlin.takeOrFillWithNulls
import studio.forface.shuttle.design.R

@Composable
fun WidgetPreview(model: WidgetPreviewUiModel) {
    val layout = model.layout

    val rows = layout.rowsCount
    val columns = layout.columnsCount
    val apps = model.apps.takeOrFillWithNulls(rows * columns).reversed()
    var index = 0

    Column(
        modifier = Modifier
            .wrapContentSize()
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.78f),
                shape = RoundedCornerShape(Dimens.Margin.Large)
            )
            .padding(horizontal = layout.horizontalSpacing, vertical = layout.verticalSpacing)
    ) {
        if (model.layout.showRefreshLocation) {
            Row(horizontalArrangement = Arrangement.End) {
                Image(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = stringResource(id = R.string.widget_refresh_button_content_description)
                )
            }
        }
        repeat(rows) {
            Row {
                repeat(columns) {
                    AppIconItem(
                        app = apps[index++],
                        widgetSettings = layout
                    )
                }
            }
        }
    }
}

@Composable
private fun AppIconItem(
    app: WidgetPreviewAppUiModel?,
    widgetSettings: WidgetLayoutUiModel
) {
    app ?: return

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            vertical = widgetSettings.verticalSpacing,
            horizontal = widgetSettings.horizontalSpacing
        )
    ) {

        Image(
            painter = rememberImagePainter(data = app.icon),
            contentDescription = stringResource(id = R.string.x_app_icon_description),
            modifier = Modifier.size(widgetSettings.iconSize)
        )
        Spacer(modifier = Modifier.height(widgetSettings.verticalSpacing))
        Text(
            text = app.name,
            maxLines = if (widgetSettings.allowTwoLines) 2 else 1,
            fontSize = widgetSettings.textSize,
            lineHeight = widgetSettings.textSize,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(widgetSettings.iconSize)
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
        showRefreshLocation = true,
        textSize = 9.sp,
        verticalSpacing = 10.dp
    )
    MaterialTheme {
        WidgetPreview(WidgetPreviewUiModel(apps = apps, layout = widgetSettings))
    }
}
