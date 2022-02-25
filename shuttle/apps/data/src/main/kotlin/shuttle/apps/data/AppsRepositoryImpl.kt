package shuttle.apps.data

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import shuttle.apps.domain.AppsRepository
import shuttle.apps.domain.error.GenericError
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.AppName
import kotlin.coroutines.coroutineContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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

    override fun observeAllInstalledApps(): Flow<Either<GenericError, List<AppModel>>> =
        flow {
            while (coroutineContext.isActive) {
                emit(getAllInstalledApps())
                delay(RefreshDelay)
            }
        }

    private fun buildLauncherCategoryIntent() =
        Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)

    private fun toAppModel(resolveInfo: ResolveInfo) = AppModel(
        AppId(resolveInfo.activityInfo.packageName),
        AppName(resolveInfo.loadLabel(packageManager).toString())
    )

    companion object {

        private val RefreshDelay = 1.toDuration(DurationUnit.MINUTES)
    }
}
