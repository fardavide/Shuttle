package studio.forface.shuttle

import android.app.Application
import studio.forface.shuttle.startup.KoinStartup
import studio.forface.shuttle.startup.StrictModeStartup
import studio.forface.shuttle.startup.SyncStartup
import studio.forface.shuttle.startup.init

class ShuttleApplication : Application(), ShuttleApplicationContext {

    override val app: ShuttleApplication
        get() = this

    override fun onCreate() {
        super.onCreate()
        init(KoinStartup, StrictModeStartup, SyncStartup)
    }
}

interface ShuttleApplicationContext {
    val app: ShuttleApplication
}
