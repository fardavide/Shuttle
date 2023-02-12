package studio.forface.shuttle.startup

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import shuttle.stats.domain.usecase.StartDeleteOldStats

object SyncStartup : Startup, KoinComponent {

    private val startDeleteOldStats: StartDeleteOldStats by inject()

    override fun init() {
        startDeleteOldStats()
    }
}
