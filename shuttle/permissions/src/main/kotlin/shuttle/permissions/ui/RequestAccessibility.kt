package shuttle.permissions.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.PreviewDimens
import shuttle.design.theme.ShuttleTheme
import shuttle.permissions.resources.Strings

@Composable
internal fun RequestAccessibility(onAccessibilityRequest: () -> Unit) {
    RequestPermissionsPage(
        title = Strings.Accessibility.Title,
        description = Strings.Accessibility.Description,
        action = Strings.Accessibility.Action,
        onAction = onAccessibilityRequest
    )
}

@Composable
@Preview(showBackground = true, widthDp = PreviewDimens.Medium.Width, heightDp = PreviewDimens.Medium.Height)
fun RequestAccessibilityPreview() {
    ShuttleTheme {
        RequestAccessibility(onAccessibilityRequest = {})
    }
}
