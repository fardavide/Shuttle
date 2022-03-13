package shuttle.permissions.model

import android.Manifest
import android.os.Build

internal const val CoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION
internal const val FineLocation = Manifest.permission.ACCESS_FINE_LOCATION
internal val BackgroundLocation =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Manifest.permission.ACCESS_BACKGROUND_LOCATION
    else ""

internal val foregroundPermissionsList = listOf(CoarseLocation, FineLocation)
internal val backgroundPermissionsList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    listOf(CoarseLocation, FineLocation, BackgroundLocation)
} else {
    foregroundPermissionsList
}
