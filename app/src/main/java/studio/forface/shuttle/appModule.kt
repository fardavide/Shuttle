package studio.forface.shuttle

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.scope.Scope
import org.koin.dsl.module
import shuttle.accessibility.StartAppId
import shuttle.di.shuttleModule

val appModule = module {

    single { CoroutineScope(Job() + Dispatchers.Default) }
    factory<PackageManager> { get<Context>().packageManager }
    factory<() -> Unit>(StartAppId) { ::startMainActivity }
    factory { WorkManager.getInstance(get()) }

} + shuttleModule

private fun Scope.startMainActivity() {
    val context: Context = get()
    val intent = Intent(context, MainActivity::class.java)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}
