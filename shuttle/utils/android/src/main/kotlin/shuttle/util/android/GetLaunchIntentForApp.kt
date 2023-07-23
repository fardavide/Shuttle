package shuttle.util.android

import android.content.Intent
import android.content.pm.PackageManager
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.GetAppError

@Factory
class GetLaunchIntentForApp(
    private val packageManager: PackageManager
) {

    operator fun invoke(appId: AppId): Either<GetAppError, Intent> =
        packageManager.getLaunchIntentForPackage(appId.value)?.right()
            ?: GetAppError.AppNotInstalled.left()
}
