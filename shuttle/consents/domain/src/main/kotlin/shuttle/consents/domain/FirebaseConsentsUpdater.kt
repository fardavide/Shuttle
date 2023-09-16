package shuttle.consents.domain

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import shuttle.settings.domain.usecase.ObserveIsDataCollectionEnabled

interface FirebaseConsentsUpdater {

    fun start()
}

@Single
internal class RealFirebaseConsentsUpdater(
    private val analytics: FirebaseAnalytics,
    private val appScope: CoroutineScope,
    private val crashlytics: FirebaseCrashlytics,
    private val performance: FirebasePerformance,
    private val observeIsDataCollectionEnabled: ObserveIsDataCollectionEnabled
) : FirebaseConsentsUpdater {

    override fun start() {
        appScope.launch {
            observeIsDataCollectionEnabled().collect { isEnabled ->
                analytics.setAnalyticsCollectionEnabled(isEnabled)
                crashlytics.setCrashlyticsCollectionEnabled(isEnabled)
                performance.isPerformanceCollectionEnabled = isEnabled
            }
        }
    }
}
