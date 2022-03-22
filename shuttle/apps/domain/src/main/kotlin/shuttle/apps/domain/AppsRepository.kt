package shuttle.apps.domain

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel

interface AppsRepository {

    /**
     * Get an installed app or [AppNotInstalled]
     */
    suspend fun getApp(id: AppId): Either<AppNotInstalled, AppModel>

    /**
     * Observe all the installed apps
     */
    fun observeAllInstalledApps(): Flow<List<AppModel>>

    /**
     * Observe all the installed icon packs
     */
    fun observeInstalledIconPacks(): Flow<List<AppModel>>

    /**
     * Observe all the installed apps that are not blacklisted
     */
    fun observeNotBlacklistedApps(): Flow<List<AppModel>>
}

object AppNotInstalled
