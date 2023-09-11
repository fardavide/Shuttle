package shuttle.accessibility.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import shuttle.accessibility.StartAppQualifier
import shuttle.accessibility.usecase.StartRefreshLocationAndUpdateWidget
import shuttle.accessibility.usecase.StartStoreIfNotBlacklistAndUpdateWidget
import shuttle.apps.domain.model.AppId
import shuttle.settings.domain.usecase.HasEnabledAccessibilityService
import shuttle.settings.domain.usecase.SetHasEnabledAccessibilityService

class LaunchCounterAccessibilityService : AccessibilityService() {

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    private val hasEnabledAccessibilityService: HasEnabledAccessibilityService by inject()
    private val setHasEnabledAccessibilityService: SetHasEnabledAccessibilityService by inject()
    private val startRefreshLocationAndUpdateWidget: StartRefreshLocationAndUpdateWidget by inject()
    private val startApp: () -> Unit by inject(named(StartAppQualifier))
    private val startStoreIfNotBlacklistAndUpdateWidget: StartStoreIfNotBlacklistAndUpdateWidget by inject()

    private var previousPackageName: CharSequence? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        fun isPackageValid(p: CharSequence?) = p.isNullOrBlank().not()
        fun isPackageChanged(p: CharSequence?) = p != previousPackageName
        fun isLauncher(p: CharSequence?) = p in LauncherPackages

        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName
            if (isPackageValid(packageName) && isPackageChanged(packageName)) {
                previousPackageName = packageName
                Log.d("LaunchCounterAccessibilityService", "Package changed: $packageName")

                if (isLauncher(packageName)) {
                    startRefreshLocationAndUpdateWidget()
                } else {
                    startStoreIfNotBlacklistAndUpdateWidget(AppId(event.packageName.toString()))
                }
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

    companion object {

        val LauncherPackages = listOf(
            "com.android.systemui",

            "com.google.android.apps.nexuslauncher",
            "com.motorola.launcher3",
            "com.sec.android.app.launcher"
        )
    }
}
