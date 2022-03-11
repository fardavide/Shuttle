package studio.forface.shuttle

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin

class ShuttleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@ShuttleApplication)
            workManagerFactory()
            modules(appModule)
        }
    }
}
