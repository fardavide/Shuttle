package shuttle.permissions.presentation.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import kotlin.random.Random

internal fun openAccessibilitySettings(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    context.startActivity(intent)
}

internal fun openLocationPermissionsOrAppSettings(context: Context) {
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
