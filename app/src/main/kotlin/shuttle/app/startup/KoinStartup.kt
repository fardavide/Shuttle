package shuttle.app.startup

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin
import org.koin.ksp.generated.module
import shuttle.app.AppModule
import shuttle.app.ShuttleApplicationContext

object KoinStartup : Startup {

    context(ShuttleApplicationContext)
    override fun init() {
        startKoin {
            androidLogger()
            androidContext(app)
            workManagerFactory()
            modules(AppModule().module)
        }
    }
}
