package shuttle.analytics.android

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@ComponentScan
class AnalyticsAndroidModule {

    @Factory
    fun firebaseAnalytics(): FirebaseAnalytics = Firebase.analytics
}
