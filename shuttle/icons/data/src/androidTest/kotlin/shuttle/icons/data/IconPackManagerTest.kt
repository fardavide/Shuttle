package shuttle.icons.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlin.test.Test
import kotlin.test.assertEquals

class IconPackManagerTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val packageManager = context.packageManager
    private val iconPackManager = IconPackManager(context, packageManager)

    @Test
    fun loadIconPacks() {
        val result = iconPackManager.getAvailableIconPacks(forceReload = true)
        assertEquals(emptyMap(), result)
    }
}
