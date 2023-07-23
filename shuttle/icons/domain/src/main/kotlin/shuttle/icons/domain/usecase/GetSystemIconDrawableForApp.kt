package shuttle.icons.domain.usecase

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.GetAppError
import shuttle.utils.kotlin.IoDispatcher

@Factory
class GetSystemIconDrawableForApp(
    private val packageManager: PackageManager,
    @Named(IoDispatcher) private val ioDispatcher: CoroutineDispatcher
) {

    private val cache = mutableMapOf<AppId, Drawable>()

    suspend operator fun invoke(id: AppId): Either<GetAppError, Drawable> = withContext(ioDispatcher) {
        cache[id]?.right()
            ?: getSystemIconFromSystem(id)
                .onRight { cache[id] = it }
    }

    private fun getSystemIconFromSystem(id: AppId): Either<GetAppError, Drawable> = try {
        packageManager.getApplicationIcon(id.value).right()
    } catch (ignored: PackageManager.NameNotFoundException) {
        GetAppError.AppNotInstalled.left()
    }
}
