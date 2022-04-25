package shuttle.utils.kotlin

import org.koin.core.qualifier.named
import org.koin.dsl.module

val utilsKotlinModule = module {

    factory { GetAppVersion(appVersion = get(AppVersionQualifier)) }
}

val AppVersionQualifier = named("App version")
