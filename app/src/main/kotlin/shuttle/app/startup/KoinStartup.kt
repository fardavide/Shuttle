package shuttle.app.startup

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin
import shuttle.app.ShuttleApplicationContext
import shuttle.app.appModule

object KoinStartup : Startup {

    context(ShuttleApplicationContext)
    override fun init() {
        startKoin {
            androidLogger()
            androidContext(app)
            workManagerFactory()
            modules(appModule)
        }
    }
}
