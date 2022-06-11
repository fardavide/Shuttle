package shuttle.design.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme

@Composable
fun LoadingSpinner(modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.size(Dimens.Icon.Large))
    }
}

@Composable
@Preview(showBackground = true)
private fun LoadingSpinnerPreview() {
    ShuttleTheme {
        LoadingSpinner()
    }
}
