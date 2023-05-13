package shuttle.design.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import shuttle.resources.R.string

@Composable
fun BackIconButton(onBack: () -> Unit) {
    IconButton(onClick = onBack) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = stringResource(id = string.x_back_button_description)
        )
    }
}
