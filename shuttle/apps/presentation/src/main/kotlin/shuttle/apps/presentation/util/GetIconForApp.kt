package shuttle.apps.presentation.util

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import arrow.core.Either
import arrow.core.right
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppId

class GetIconForApp(
    private val packageManager: PackageManager
) {

    operator fun invoke(id: AppId): Drawable =
        packageManager.getApplicationIcon(id.value)
}
