package shuttle.util.android

import org.koin.dsl.module

val utilsAndroidModule = module {

    factory { GetIconDrawableForApp(packageManager = get()) }
    factory { GetIconForApp(packageManager = get()) }
    factory { GetLaunchIntentForApp(packageManager = get()) }
}
