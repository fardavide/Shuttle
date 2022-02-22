package studio.forface.shuttle

import android.content.Context
import android.content.pm.PackageManager
import org.koin.dsl.module
import shuttle.di.shuttleModule

val appModule = module {

    factory<PackageManager> { get<Context>().packageManager }

} + shuttleModule
