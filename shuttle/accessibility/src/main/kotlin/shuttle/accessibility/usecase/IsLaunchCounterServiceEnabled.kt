package shuttle.accessibility.usecase

import android.content.ComponentName
import android.content.ContentResolver
import android.provider.Settings
import android.text.TextUtils

class IsLaunchCounterServiceEnabled(
    private val accessibilityServiceComponentName: ComponentName,
    private val contentResolver: ContentResolver
) {

    operator fun invoke(): Boolean {
        val enabledServicesSetting: String =
            Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
                ?: return false
        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServicesSetting)
        while (colonSplitter.hasNext()) {
            val componentNameString: String = colonSplitter.next()
            val enabledService = ComponentName.unflattenFromString(componentNameString)
            if (enabledService != null && enabledService == accessibilityServiceComponentName) return true
        }
        return false
    }
}
