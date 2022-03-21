package shuttle.util.android

import org.koin.dsl.module

val utilsAndroidModule = module {

    factory { GetLaunchIntentForApp(packageManager = get()) }
    factory { IsAndroidQ() }
}
