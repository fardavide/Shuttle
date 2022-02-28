package shuttle.util.android

import android.content.Intent
import android.content.pm.PackageManager
import shuttle.apps.domain.model.AppId

class GetLaunchIntentForApp(
    private val packageManager: PackageManager
) {

    operator fun invoke(appId: AppId): Intent =
        packageManager.getLaunchIntentForPackage(appId.value)!!
}
