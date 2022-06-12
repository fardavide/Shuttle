package shuttle.design.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import studio.forface.shuttle.design.R.drawable
import studio.forface.shuttle.design.R.string

@Composable
fun NavigableListItem(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    NavigableListItem(
        title = stringResource(id = title),
        iconPainter = painterResource(id = icon),
        onClick = onClick
    )
}

@Composable
fun NavigableListItem(
    title: String,
    iconPainter : Painter,
    onClick: () -> Unit
) {
    val contentDescription = stringResource(id = string.navigable_item_go_to, title)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = Dimens.Margin.Medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .padding(Dimens.Margin.Medium)
                    .size(Dimens.Icon.Small),
                painter = iconPainter,
                contentDescription = contentDescription
            )
            Spacer(modifier = Modifier.width(Dimens.Margin.Medium))
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        }
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = contentDescription
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun NavigableListItemPreview() {
    ShuttleTheme {
        NavigableListItem(
            title = string.settings_widget_layout_title,
            icon = drawable.ic_grid,
            onClick = {}
        )
    }
}
