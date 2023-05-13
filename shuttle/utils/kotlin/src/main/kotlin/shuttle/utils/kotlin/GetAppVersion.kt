package shuttle.utils.kotlin

import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
class GetAppVersion(
    @Named(AppVersionQualifier) private val appVersion: Int
) {

    operator fun invoke(): Int = appVersion
}
