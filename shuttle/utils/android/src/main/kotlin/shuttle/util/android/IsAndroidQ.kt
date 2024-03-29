package shuttle.util.android

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import org.koin.core.annotation.Factory

@Factory
class IsAndroidQ {

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    operator fun invoke(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}
