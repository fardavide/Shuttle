package shuttle.design.ui

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import shuttle.design.model.TextRes
import shuttle.design.model.stringResource
import shuttle.design.theme.Dimens

object CheckableListItem {

    @Composable
    operator fun invoke(
        title: TextRes,
        id: Any = title,
        icon: Painter,
        contentDescription: TextRes?,
        isChecked: Boolean,
        onCheckChange: (isChecked: Boolean) -> Unit
    ) {
        Internal(
            title = title,
            id = id,
            icon = icon,
            contentDescription = contentDescription,
            isChecked = isChecked,
            onCheckChange = onCheckChange,
            iconSize = Dimens.Icon.Small,
            iconPadding = Dimens.Margin.Medium
        )
    }

    @Composable
    fun LargeIcon(
        title: TextRes,
        id: Any = title,
        icon: Painter,
        contentDescription: TextRes?,
        isChecked: Boolean,
        onCheckChange: (isChecked: Boolean) -> Unit
    ) {
        Internal(
            title = title,
            id = id,
            icon = icon,
            contentDescription = contentDescription,
            isChecked = isChecked,
            onCheckChange = onCheckChange,
            iconSize = Dimens.Icon.Medium,
            iconPadding = 0.dp
        )
    }

    @Composable
    operator fun invoke(
        title: TextRes,
        id: Any = title,
        iconDrawable: Drawable,
        contentDescription: TextRes?,
        isChecked: Boolean,
        onCheckChange: (isChecked: Boolean) -> Unit
    ) {
        CheckableListItem(
            title = title,
            id = id,
            icon = rememberImagePainter(data = iconDrawable),
            contentDescription = contentDescription,
            isChecked = isChecked,
            onCheckChange = onCheckChange
        )
    }

    @Composable
    fun LargeIcon(
        title: TextRes,
        id: Any = title,
        iconDrawable: Drawable,
        contentDescription: TextRes?,
        isChecked: Boolean,
        onCheckChange: (isChecked: Boolean) -> Unit
    ) {
        LargeIcon(
            title = title,
            id = id,
            icon = rememberImagePainter(data = iconDrawable),
            contentDescription = contentDescription,
            isChecked = isChecked,
            onCheckChange = onCheckChange
        )
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun Internal(
        title: TextRes,
        id: Any = title,
        icon: Painter,
        contentDescription: TextRes?,
        isChecked: Boolean,
        onCheckChange: (isChecked: Boolean) -> Unit,
        iconSize: Dp,
        iconPadding: Dp
    ) {
        var checkedState by remember(id) { mutableStateOf(isChecked) }
        val toggleAction = { isCheckboxChecked: Boolean ->
            checkedState = isCheckboxChecked
            onCheckChange(isCheckboxChecked)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    top = Dimens.Margin.XXSmall,
                    bottom = Dimens.Margin.XXSmall,
                    start = Dimens.Margin.Medium,
                    end = Dimens.Margin.Small
                )
                .clickable { toggleAction(checkedState.not()) }
        ) {
            Image(
                modifier = Modifier.padding(iconPadding).size(iconSize),
                painter = icon,
                contentDescription = contentDescription?.let { stringResource(textRes = it) }
            )
            Spacer(modifier = Modifier.width(Dimens.Margin.Medium))
            Text(
                text = stringResource(textRes = title),
                style = MaterialTheme.typography.titleMedium
            )
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                Checkbox(checked = checkedState, onCheckedChange = toggleAction)
            }
        }
    }
}
