package shuttle.predictions.presentation.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
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
import shuttle.predictions.presentation.mapper.LocationPermissionsStateMapper
import shuttle.predictions.presentation.model.LocationPermissionsState
import shuttle.predictions.presentation.model.LocationPermissionsState.AllGranted
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.AllDenied
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.CoarseOnly
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.Init
import shuttle.predictions.presentation.model.LocationPermissionsState.Pending.MissingBackground
import shuttle.predictions.presentation.model.backgroundPermissionsList
import shuttle.predictions.presentation.model.foregroundPermissionsList
import shuttle.predictions.presentation.resources.Strings
import kotlin.random.Random

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun LocationPermissionsPage(onAllPermissionsGranted: () -> Unit) {
    val context = LocalContext.current
    val mapper = LocationPermissionsStateMapper()

    val foregroundLocationPermissionsState = rememberMultiplePermissionsState(foregroundPermissionsList)
    val backgroundLocationPermissionsState = rememberMultiplePermissionsState(backgroundPermissionsList)

    when (val state = mapper.toLocationPermissionState(backgroundLocationPermissionsState)) {
        AllGranted -> {
            LaunchedEffect(key1 = state) { onAllPermissionsGranted() }
        }
        is LocationPermissionsState.Pending -> RequestPermissions(state) {
            when (state) {
                AllDenied, MissingBackground -> openSettingsPage(context)
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

private fun openSettingsPage(context: Context) {
    val activity: Activity = when (context) {
        is Activity -> context
        is ContextWrapper -> context.baseContext as Activity
        else -> throw IllegalStateException("Context is not an activity")
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        activity.requestPermissions(
            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            Random.nextInt(0, Int.MAX_VALUE)
        )
    } else {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(Uri.fromParts("package", activity.packageName, null))
        activity.startActivity(intent)
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
