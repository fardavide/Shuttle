package shuttle.analytics

import org.koin.core.annotation.Factory

interface LaunchersLogger {

    fun logInstalledLaunchers(packageNames: List<String>)
}

@Factory
internal class RealLaunchersLogger(
    private val analytics: Analytics
) : LaunchersLogger {

    override fun logInstalledLaunchers(packageNames: List<String>) {
        analytics.log(
            event("installed_launchers") {
                "count" withValue packageNames.size
                "package_names" withValue packageNames
            }
        )
    }
}
