package shuttle.accessibility

import android.content.ComponentName
import android.content.Context
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import shuttle.accessibility.service.LaunchCounterAccessibilityService

@Module
@ComponentScan
class AccessibilityModule {

    @Factory
    @Named(AccessibilityServiceComponentName)
    fun accessibilityServiceComponentName(context: Context) =
        ComponentName(context, LaunchCounterAccessibilityService::class.java)
}

const val StartAppQualifier = "Start app"

internal const val AccessibilityServiceComponentName = "Accessibility service component name"
