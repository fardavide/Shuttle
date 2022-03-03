package shuttle.permissions.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import shuttle.design.theme.Dimens
import shuttle.design.theme.ShuttleTheme
import shuttle.permissions.mapper.LocationPermissionsStateMapper
import shuttle.permissions.model.LocationPermissionsState
import shuttle.permissions.model.LocationPermissionsState.AllGranted
import shuttle.permissions.model.LocationPermissionsState.Pending.AllDenied
import shuttle.permissions.model.LocationPermissionsState.Pending.CoarseOnly
import shuttle.permissions.model.LocationPermissionsState.Pending.Init
import shuttle.permissions.model.LocationPermissionsState.Pending.MissingBackground
import shuttle.permissions.model.backgroundPermissionsList
import shuttle.permissions.model.foregroundPermissionsList
import shuttle.permissions.resources.Strings
import shuttle.permissions.util.openLocationPermissionsOrAppSettings

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun LocationPermissionsPage(onAllPermissionsGranted: () -> Unit) {
    val context = LocalContext.current
    val mapper = LocationPermissionsStateMapper()

    val foregroundLocationPermissionsState = rememberMultiplePermissionsState(foregroundPermissionsList)
    val backgroundLocationPermissionsState = rememberMultiplePermissionsState(backgroundPermissionsList)

    when (val state = mapper.toLocationPermissionState(backgroundLocationPermissionsState)) {
        AllGranted -> {
            LaunchedEffect(state) { onAllPermissionsGranted() }
        }
        is LocationPermissionsState.Pending -> RequestPermissions(state) {
            when (state) {
                AllDenied, MissingBackground -> openLocationPermissionsOrAppSettings(context)
                Init, CoarseOnly -> foregroundLocationPermissionsState.launchMultiplePermissionRequest()
            }
        }
    }
}

@Composable
internal fun RequestPermissions(state: LocationPermissionsState.Pending, onPermissionRequest: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Margin.XXLarge)
    ) {
        val (message, buttonText) = when (state) {
            Init -> Strings.Message.RequestLocation to Strings.Action.RequestPermissions
            CoarseOnly -> Strings.Message.RequestPreciseLocation to Strings.Action.AllowPreciseLocation
            MissingBackground -> Strings.Message.RequestBackgroundLocation to Strings.Action.OpenLocationSettings
            AllDenied -> Strings.Message.LocationFeatureDisabled to Strings.Action.OpenLocationSettings
        }

        Text(
            text = message,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Justify,
        )
        Row(horizontalArrangement = Arrangement.End) {
            Button(onClick = onPermissionRequest) {
                Text(buttonText)
            }
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 700, heightDp = 250)
fun InitPermissionsPreview() {
    ShuttleTheme {
        RequestPermissions(state = Init, onPermissionRequest = {})
    }
}

@Composable
@Preview(showBackground = true, widthDp = 700, heightDp = 250)
fun CoarseOnlyPermissionsPreview() {
    ShuttleTheme {
        RequestPermissions(state = CoarseOnly, onPermissionRequest = {})
    }
}

@Composable
@Preview(showBackground = true, widthDp = 700, heightDp = 250)
fun MissingBackgroundOnlyPermissionsPreview() {
    ShuttleTheme {
        RequestPermissions(state = MissingBackground, onPermissionRequest = {})
    }
}

@Composable
@Preview(showBackground = true, widthDp = 700, heightDp = 250)
fun AllDeniedPermissionsPreview() {
    ShuttleTheme {
        RequestPermissions(state = AllDenied, onPermissionRequest = {})
    }
}
