package shuttle.analytics

import org.koin.core.annotation.Factory
import shuttle.launchers.Launchers

interface LaunchersLogger {

    fun logLaunchers(packageNames: List<String>)
}

@Factory
internal class RealLaunchersLogger(
    private val analytics: Analytics
) : LaunchersLogger {

    override fun logLaunchers(packageNames: List<String>) {
        for (launcher in packageNames) {
            analytics.log(
                event("installed_launcher") {
                    "installed_launcher_package" withValue launcher
                }
            )
        }
        for (unknownLauncher in packageNames.filterNot { it in Launchers.all() }) {
            analytics.log(
                event("unknown_launcher") {
                    "unknown_launcher_package" withValue unknownLauncher
                }
            )
        }
    }
}
