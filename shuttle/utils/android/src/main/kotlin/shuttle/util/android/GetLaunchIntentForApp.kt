package shuttle.util.android

import android.content.Intent
import android.content.pm.PackageManager
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId

@Factory
class GetLaunchIntentForApp(
    private val packageManager: PackageManager
) {

    operator fun invoke(appId: AppId): Intent = packageManager.getLaunchIntentForPackage(appId.value)!!
}
