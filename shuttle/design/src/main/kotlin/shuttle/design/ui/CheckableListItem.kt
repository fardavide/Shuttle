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
import coil.compose.rememberImagePainter
import shuttle.design.theme.Dimens

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CheckableListItem(
    title: String,
    id: Any = title,
    iconDrawable: Drawable,
    contentDescription: String?,
    isChecked: Boolean,
    onCheckChange: (isChecked: Boolean) -> Unit
) {
    var checkedState by remember(id) { mutableStateOf(isChecked) }
    val toggleAction = { isCheckboxChecked: Boolean ->
        checkedState = isCheckboxChecked
        onCheckChange(isCheckboxChecked)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = Dimens.Margin.XXSmall, horizontal = Dimens.Margin.Small)
            .clickable { toggleAction(checkedState.not()) }
    ) {
        Image(
            painter = rememberImagePainter(data = iconDrawable),
            contentDescription = contentDescription,
            modifier = Modifier.size(Dimens.Icon.Medium)
        )
        Spacer(modifier = Modifier.width(Dimens.Margin.Medium))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
            Checkbox(checked = checkedState, onCheckedChange = toggleAction)
        }
    }
}
