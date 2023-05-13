package shuttle.apps.domain.repository

import kotlinx.coroutines.flow.Flow
import shuttle.apps.domain.model.AppModel

interface AppsRepository {

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
