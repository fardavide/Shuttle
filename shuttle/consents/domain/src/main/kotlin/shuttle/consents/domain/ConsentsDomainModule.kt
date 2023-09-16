package shuttle.consents.domain

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@ComponentScan
class ConsentsDomainModule {

    @Factory
    fun crashlytics(): FirebaseCrashlytics = Firebase.crashlytics
}
