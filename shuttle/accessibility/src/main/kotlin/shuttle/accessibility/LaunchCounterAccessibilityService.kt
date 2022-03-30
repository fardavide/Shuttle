package shuttle.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import shuttle.accessibility.usecase.IncrementOpenCounterIfNotBlacklisted
import shuttle.accessibility.usecase.UpdateWidget
import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.usecase.HasEnabledAccessibilityService
import shuttle.settings.domain.usecase.SetHasEnabledAccessibilityService

class LaunchCounterAccessibilityService: AccessibilityService() {

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    private val hasEnabledAccessibilityService: HasEnabledAccessibilityService by inject()
    private val incrementOpenCounterIfNotBlacklisted: IncrementOpenCounterIfNotBlacklisted by inject()
    private val setHasEnabledAccessibilityService: SetHasEnabledAccessibilityService by inject()
    private val startApp: () -> Unit by inject(StartAppId)
    private val updateWidget: UpdateWidget by inject()

    private var previousPackageName: CharSequence? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        fun isPackageValid(p: CharSequence?) = p.isNullOrBlank().not()
        fun isPackageChanged(p: CharSequence?) = p != previousPackageName

        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName
            if (isPackageValid(packageName) && isPackageChanged(packageName)) {
                previousPackageName = packageName
                Log.d("LaunchCounterAccessibilityService", "Package changed: $packageName")

                incrementOpenCounterIfNotBlacklisted(AppId(event.packageName.toString()))
                updateWidget()
            }
        }
    }

    override fun onServiceConnected() {
        scope.launch {
            if (hasEnabledAccessibilityService().not()) {
                startApp()
            }
            setHasEnabledAccessibilityService()
        }
    }

    override fun onInterrupt() {}
}
