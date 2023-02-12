package shuttle.icons.domain.usecase

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.error.GetSystemIconError

class GetSystemIconDrawableForApp(
    private val packageManager: PackageManager,
    private val ioDispatcher: CoroutineDispatcher
) {

    private val cache = mutableMapOf<AppId, Drawable>()

    suspend operator fun invoke(id: AppId): Either<GetSystemIconError, Drawable> = withContext(ioDispatcher) {
        cache[id]?.right()
            ?: getSystemIconFromSystem(id)
                .onRight { cache[id] = it }
    }

    private fun getSystemIconFromSystem(id: AppId): Either<GetSystemIconError, Drawable> = try {
        packageManager.getApplicationIcon(id.value).right()
    } catch (ignored: PackageManager.NameNotFoundException) {
        GetSystemIconError.AppNotInstalled.left()
    }
}
