package studio.forface.shuttle

import android.content.Context
import android.content.pm.PackageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.dsl.module
import shuttle.di.shuttleModule

val appModule = module {

    factory<PackageManager> { get<Context>().packageManager }
    single { CoroutineScope(Job() + Dispatchers.Default) }

} + shuttleModule
