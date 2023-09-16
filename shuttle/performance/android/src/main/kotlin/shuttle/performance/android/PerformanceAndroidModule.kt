package shuttle.performance.android

import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.ktx.performance
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@ComponentScan
class PerformanceAndroidModule {

    @Factory
    fun firebasePerformance(): FirebasePerformance = Firebase.performance
}
