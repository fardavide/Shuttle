package shuttle.predictions.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import shuttle.design.ShuttleTheme
import shuttle.predictions.presentation.mapper.LocationPermissionsStateMapper
import shuttle.predictions.presentation.model.LocationPermissionsState
import shuttle.predictions.presentation.model.LocationPermissionsState.AllGranted
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.AllDenied
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.CoarseOnly
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.Init
import shuttle.predictions.presentation.resources.Strings

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun LocationPermissionsScreen(onAllPermissionsGranted: () -> Unit) {
    val mapper = LocationPermissionsStateMapper()
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    when (val state = mapper.toLocationPermissionState(locationPermissionsState)) {
        AllGranted -> onAllPermissionsGranted()
        is LocationPermissionsState.Pending -> RequestPermissions(state) {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }
}

@Composable
internal fun RequestPermissions(state: LocationPermissionsState.Pending, onPermissionRequest: () -> Unit) {
    Column {
        val textToShow = when(state) {
            Init -> Strings.Message.RequestLocation
            CoarseOnly -> Strings.Message.RequestPreciseLocation
            AllDenied -> Strings.Message.LocationFeatureDisabled
        }

        val buttonText = if (state == CoarseOnly) {
            Strings.Action.AllowPreciseLocation
        } else {
            Strings.Action.RequestPermissions
        }

        Text(text = textToShow)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onPermissionRequest) {
            Text(buttonText)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun InitPermissionsPreview() {
    ShuttleTheme {
        RequestPermissions(state = Init, onPermissionRequest = {})
    }
}

@Composable
@Preview(showBackground = true)
fun CoarseOnlyPermissionsPreview() {
    ShuttleTheme {
        RequestPermissions(state = CoarseOnly, onPermissionRequest = {})
    }
}

@Composable
@Preview(showBackground = true)
fun AllDeniedPermissionsPreview() {
    ShuttleTheme {
        RequestPermissions(state = AllDenied, onPermissionRequest = {})
    }
}
