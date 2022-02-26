package shuttle.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import org.koin.android.ext.android.inject
import shuttle.accessibility.usecase.IncrementOpenCounter
import shuttle.apps.domain.model.AppId

internal class LaunchCounterAccessibilityService : AccessibilityService() {

    private val incrementOpenCounter: IncrementOpenCounter by inject()
    private var previousPackageName: CharSequence? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        fun isPackageValid(p: CharSequence?) = p.isNullOrBlank().not()
        fun isPackageChanged(p: CharSequence?) = p != previousPackageName

        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName
            if (isPackageValid(packageName) && isPackageChanged(packageName)) {
                previousPackageName = packageName
                Log.d("LaunchCounterAccessibilityService", "Package changed: $packageName")
                incrementOpenCounter(AppId(event.packageName.toString()))
            }
        }
    }

    override fun onInterrupt() {

    }
}
