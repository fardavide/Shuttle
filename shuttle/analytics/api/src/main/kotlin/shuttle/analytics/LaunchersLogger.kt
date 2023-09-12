package shuttle.analytics

import org.koin.core.annotation.Factory
import shuttle.launchers.Launchers

interface LaunchersLogger {

    fun logUnknownLaunchers(packageNames: List<String>)
}

@Factory
internal class RealLaunchersLogger(
    private val analytics: Analytics
) : LaunchersLogger {

    override fun logUnknownLaunchers(packageNames: List<String>) {
        val unknownLaunchers = packageNames.filterNot { it in Launchers.all() }
        if (unknownLaunchers.isEmpty()) return

        analytics.log(
            event("unknown_launchers") {
                "count" withValue unknownLaunchers.size
                "package_names" withValue unknownLaunchers
            }
        )
    }
}
