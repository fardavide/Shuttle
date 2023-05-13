package shuttle.app

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.work.WorkManager
import co.touchlab.kermit.Logger
import co.touchlab.kermit.LoggerConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import shuttle.accessibility.StartAppQualifier
import shuttle.di.AppVersionQualifier
import shuttle.di.shuttleModule
import studio.forface.shuttle.BuildConfig

val appModule = module {

    single(named(AppVersionQualifier)) { BuildConfig.VERSION_CODE }
    single { CoroutineScope(Job() + Dispatchers.Default) }
    single { Logger(LoggerConfig.default) }
    factory<PackageManager> { get<Context>().packageManager }
    factory { get<Context>().resources }
    factory(StartAppQualifier) { startMainActivity }
    factory { WorkManager.getInstance(get()) }

} + shuttleModule

context (Scope)
private val Scope.startMainActivity: () -> Unit
    get() = {
        val context: Context = get()
        val intent = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
