package shuttle.app.startup

import com.google.firebase.FirebaseApp
import shuttle.app.ShuttleApplicationContext

object FirebaseStartup : Startup {

    context(ShuttleApplicationContext) override fun init() {
        FirebaseApp.initializeApp(app)
    }
}
