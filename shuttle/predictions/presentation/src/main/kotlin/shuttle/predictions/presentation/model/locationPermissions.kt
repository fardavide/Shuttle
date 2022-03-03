package shuttle.predictions.presentation.model

import android.Manifest
import android.os.Build

internal val foregroundPermissionsList = listOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
)
internal val backgroundPermissionsList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )
} else {
    foregroundPermissionsList
}
