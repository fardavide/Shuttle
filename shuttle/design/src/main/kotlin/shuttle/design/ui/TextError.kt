package shuttle.design.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme

@Composable
fun TextError(text: String) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(Dimens.Margin.Large)) {
        Text(textAlign = TextAlign.Justify, style = MaterialTheme.typography.headlineMedium, text = text)
    }
}

@Composable
@Preview(showBackground = true)
private fun ShortTextErrorPreview() {
    ShuttleTheme {
        TextError(text = "Location not available")
    }
}

@Composable
@Preview(showBackground = true)
private fun LongTextErrorPreview() {
    ShuttleTheme {
        TextError(text = "Location not available. Please make sure that location is turned on in your device Settings")
    }
}
