package studio.forface.shuttle.startup

import android.os.StrictMode
import studio.forface.shuttle.BuildConfig

object StrictModeStartup : Startup {

    override fun init() {
        if (BuildConfig.DEBUG) {
            val threadPolicy = StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().penaltyDeath().build()
            val vmPolicy = StrictMode.VmPolicy.Builder().detectAll().penaltyLog().penaltyDeath().build()
            StrictMode.setThreadPolicy(threadPolicy)
            StrictMode.setVmPolicy(vmPolicy)
        }
    }
}
