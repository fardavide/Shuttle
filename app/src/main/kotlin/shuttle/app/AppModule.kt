package shuttle.app

import android.content.Context
import android.content.Intent
import androidx.work.WorkManager
import co.touchlab.kermit.Logger
import co.touchlab.kermit.LoggerConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import shuttle.accessibility.StartAppQualifier
import shuttle.di.AppVersionQualifier
import shuttle.di.ShuttleModule
import studio.forface.shuttle.BuildConfig

@Module(includes = [ShuttleModule::class])
@ComponentScan
class AppModule {

    @Single
    @Named(AppVersionQualifier)
    fun appVersion(): Int = BuildConfig.VERSION_CODE

    @Factory
    fun contentResolver(context: Context) = context.contentResolver

    @Single
    fun coroutineScope() = CoroutineScope(Job() + Dispatchers.Default)

    @Single
    fun logger() = Logger(LoggerConfig.default)

    @Factory
    fun packageManager(context: Context) = context.packageManager

    @Factory
    fun resources(context: Context) = context.resources

    @Factory
    @Named(StartAppQualifier)
    fun startApp(context: Context): () -> Unit = {
        val intent = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    @Factory
    fun workManager(context: Context) = WorkManager.getInstance(context)
}

@Factory internal class Empty
