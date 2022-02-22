package shuttle.apps.data

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import shuttle.apps.domain.AppsRepository
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.AppName

class AppsRepositoryImpl(
    private val packageManager: PackageManager,
    private val ioDispatcher: CoroutineDispatcher
) : AppsRepository {

    @SuppressLint("QueryPermissionsNeeded")
    override suspend fun getAllInstalledApps(): Either<GenericError, List<AppModel>> =
        withContext(ioDispatcher) {
            packageManager.queryIntentActivities(buildLauncherCategoryIntent(), PackageManager.GET_META_DATA)
                .map(::toAppModel)
                .sortedBy { it.name.value.uppercase() }
                .right()
        }

    private fun buildLauncherCategoryIntent() =
        Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)

    private fun toAppModel(resolveInfo: ResolveInfo) = AppModel(
        AppId(resolveInfo.activityInfo.packageName),
        AppName(resolveInfo.loadLabel(packageManager).toString())
    )
}
