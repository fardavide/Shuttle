package shuttle.settings.presentation.ui.component

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import shuttle.design.theme.Dimens
import studio.forface.shuttle.design.R

@Composable
internal fun SectionItem(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    val titleString = stringResource(id = title)
    val contentDescription = stringResource(id = R.string.settings_widget_go_to, titleString)
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
                painter = painterResource(id = icon),
                contentDescription = contentDescription
            )
            Spacer(modifier = Modifier.width(Dimens.Margin.Medium))
            Text(text = titleString, style = MaterialTheme.typography.titleMedium)
        }
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = contentDescription
        )
    }
}
