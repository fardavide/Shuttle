package shuttle.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import org.koin.android.ext.android.inject
import shuttle.accessibility.usecase.IncrementOpenCounter
import shuttle.apps.domain.model.AppId

internal class LaunchCounterAccessibilityService : AccessibilityService() {

    private val incrementOpenCounter: IncrementOpenCounter by inject()
    private var previousPackageName: String? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        fun isActiveWindow() = event.source.window.isActive
        fun isPackageValid(p: CharSequence?) = p.isNullOrBlank().not()
        fun isPackageChanged(p: CharSequence?) = p != previousPackageName

        if (event.eventType == AccessibilityEvent.TYPE_WINDOWS_CHANGED) {
            val packageName = event.source.packageName
            if (isActiveWindow() && isPackageValid(packageName) && isPackageChanged(packageName)) {
                TODO("Ensure code is correct first")
                incrementOpenCounter(AppId(event.packageName.toString()))
            }
        }
    }

    override fun onInterrupt() {

    }
}
