package shuttle.util.android

import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import shuttle.apps.domain.model.AppId

class GetSystemIconForApp(
    private val packageManager: PackageManager
) {

    operator fun invoke(appId: AppId): Icon {
        val iconRes = packageManager.getApplicationInfo(appId.value, PackageManager.GET_META_DATA).icon
        return Icon.createWithResource(appId.value, iconRes)
    }
}
