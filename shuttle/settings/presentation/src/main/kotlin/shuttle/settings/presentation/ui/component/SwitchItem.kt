package shuttle.settings.presentation.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import shuttle.design.theme.Dimens

@Composable
internal fun SwitchItem(
    @StringRes title: Int,
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    var state by remember(key1 = title) { mutableStateOf(value) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.Margin.Small, horizontal = Dimens.Margin.Medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = title), style = MaterialTheme.typography.titleMedium)
        Switch(
            checked = state,
            onCheckedChange = {
                state = it
                onValueChange(it)
            }
        )
    }
}
