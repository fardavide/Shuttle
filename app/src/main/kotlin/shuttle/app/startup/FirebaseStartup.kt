package shuttle.app.startup

import com.google.firebase.FirebaseApp
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import shuttle.app.ShuttleApplicationContext
import shuttle.consents.domain.FirebaseConsentsUpdater

object FirebaseStartup : Startup, KoinComponent {

    private val firebaseConsentsUpdater: FirebaseConsentsUpdater by inject()

    context(ShuttleApplicationContext) override fun init() {
        FirebaseApp.initializeApp(app)
        firebaseConsentsUpdater.start()
    }
}
