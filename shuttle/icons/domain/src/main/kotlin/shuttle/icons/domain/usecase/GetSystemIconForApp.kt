package shuttle.icons.domain.usecase

import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import shuttle.apps.domain.model.AppId

class GetSystemIconForApp(
    private val packageManager: PackageManager,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(appId: AppId): Icon =
        withContext(ioDispatcher) {
            val iconRes = packageManager.getApplicationInfo(appId.value, PackageManager.GET_META_DATA).icon
            Icon.createWithResource(appId.value, iconRes)
        }
}
