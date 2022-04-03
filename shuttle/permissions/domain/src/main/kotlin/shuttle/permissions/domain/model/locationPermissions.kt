package shuttle.permissions.domain.model

import android.Manifest
import android.os.Build

const val CoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION
const val FineLocation = Manifest.permission.ACCESS_FINE_LOCATION
val BackgroundLocation =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Manifest.permission.ACCESS_BACKGROUND_LOCATION
    else ""

val foregroundPermissionsList = listOf(CoarseLocation, FineLocation)
val backgroundPermissionsList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    listOf(CoarseLocation, FineLocation, BackgroundLocation)
} else {
    foregroundPermissionsList
}
