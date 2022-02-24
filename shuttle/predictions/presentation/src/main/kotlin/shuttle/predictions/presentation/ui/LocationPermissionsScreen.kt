package shuttle.predictions.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import shuttle.predictions.presentation.resources.Strings

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun LocationPermissionsScreen(onAllPermissionsGranted: () -> Unit) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    if (locationPermissionsState.allPermissionsGranted) {
        onAllPermissionsGranted()
    } else {
        Column {
            val allPermissionsRevoked =
                locationPermissionsState.permissions.size ==
                    locationPermissionsState.revokedPermissions.size

            val textToShow = when {
                allPermissionsRevoked.not() -> {
                    // If not all the permissions are revoked, it's because the user accepted the COARSE
                    // location permission, but not the FINE one.
                    Strings.Message.RequestPreciseLocation
                }
                locationPermissionsState.shouldShowRationale -> {
                    // Both location permissions have been denied
                    Strings.Message.LocationFeatureDisabled
                }
                else -> {
                    // First time the user sees this feature or the user doesn't want to be asked again
                    Strings.Message.RequestLocation
                }
            }

            val buttonText = if (allPermissionsRevoked.not()) {
                Strings.Action.AllowPreciseLocation
            } else {
                Strings.Action.RequestPermissions
            }

            Text(text = textToShow)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
                Text(buttonText)
            }
        }
    }
}
