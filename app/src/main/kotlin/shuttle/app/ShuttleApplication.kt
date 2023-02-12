package shuttle.app

import android.app.Application
import shuttle.app.startup.KoinStartup
import shuttle.app.startup.StrictModeStartup
import shuttle.app.startup.SyncStartup
import shuttle.app.startup.init

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
