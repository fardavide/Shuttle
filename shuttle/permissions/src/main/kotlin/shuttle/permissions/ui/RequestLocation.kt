package shuttle.permissions.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import shuttle.design.PreviewDimens
import shuttle.design.theme.ShuttleTheme
import shuttle.permissions.model.LocationPermissionsState
import shuttle.permissions.model.LocationPermissionsState.Pending.AllDenied
import shuttle.permissions.model.LocationPermissionsState.Pending.CoarseOnly
import shuttle.permissions.model.LocationPermissionsState.Pending.Init
import shuttle.permissions.model.LocationPermissionsState.Pending.MissingBackground
import shuttle.permissions.resources.Strings

@Composable
internal fun RequestLocationPermission(state: LocationPermissionsState.Pending, onPermissionRequest: () -> Unit, onLocationPermissionsOrAppSettings: () -> Unit) {
    RequestPermissions(state) {
        when (state) {
            AllDenied, MissingBackground -> onLocationPermissionsOrAppSettings()
            Init, CoarseOnly -> onPermissionRequest()
        }
    }
}

@Composable
private fun RequestPermissions(state: LocationPermissionsState.Pending, onPermissionRequest: () -> Unit) {
    val (title, description) = when (state) {
        Init, AllDenied -> Strings.Location.Coarse.Title to Strings.Location.Coarse.Description
        CoarseOnly -> Strings.Location.Fine.Title to Strings.Location.Fine.Description
        MissingBackground -> Strings.Location.Background.Title to Strings.Location.Background.Description
    }

    RequestPermissionsPage(
        title = title,
        description = description,
        action = Strings.Location.Action,
        onAction = onPermissionRequest
    )
}

@Composable
@Preview(showBackground = true, widthDp = PreviewDimens.Medium.Width, heightDp = PreviewDimens.Medium.Height)
fun InitPermissionsPreview() {
    ShuttleTheme {
        RequestPermissions(state = Init, onPermissionRequest = {})
    }
}

@Composable
@Preview(showBackground = true, widthDp = PreviewDimens.Medium.Width, heightDp = PreviewDimens.Medium.Height)
fun CoarseOnlyPermissionsPreview() {
    ShuttleTheme {
        RequestPermissions(state = CoarseOnly, onPermissionRequest = {})
    }
}

@Composable
@Preview(showBackground = true, widthDp = PreviewDimens.Medium.Width, heightDp = PreviewDimens.Medium.Height)
fun MissingBackgroundOnlyPermissionsPreview() {
    ShuttleTheme {
        RequestPermissions(state = MissingBackground, onPermissionRequest = {})
    }
}

@Composable
@Preview(showBackground = true, widthDp = PreviewDimens.Medium.Width, heightDp = PreviewDimens.Medium.Height)
fun AllDeniedPermissionsPreview() {
    ShuttleTheme {
        RequestPermissions(state = AllDenied, onPermissionRequest = {})
    }
}
