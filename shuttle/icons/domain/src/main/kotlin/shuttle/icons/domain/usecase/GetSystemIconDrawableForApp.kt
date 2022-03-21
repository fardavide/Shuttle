package shuttle.icons.domain.usecase

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import shuttle.apps.domain.model.AppId

class GetSystemIconDrawableForApp(
    private val packageManager: PackageManager,
    private val ioDispatcher: CoroutineDispatcher
) {

    private val cache = mutableMapOf<AppId, Drawable>()

    suspend operator fun invoke(id: AppId): Drawable =
        withContext(ioDispatcher) {
            blocking(id)
        }

    fun blocking(id: AppId): Drawable =
        cache.getOrPut(id) {
            packageManager.getApplicationIcon(id.value)
        }
}
