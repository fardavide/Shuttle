package shuttle.util.android

import org.koin.dsl.module

val utilsAndroidModule = module {

    factory { GetSystemIconDrawableForApp(packageManager = get()) }
    factory { GetSystemIconForApp(packageManager = get()) }
    factory { GetLaunchIntentForApp(packageManager = get()) }
    factory { IsAndroidQ() }
}
