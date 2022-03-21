package shuttle.icons.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import shuttle.apps.domain.AppsRepository
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.AppName
import kotlin.test.Test
import kotlin.test.assertEquals

class IconPackManagerTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val packageManager = context.packageManager
    private val appsRepository: AppsRepository = mockk() {
        every { observeInstalledIconPacks() } returns flowOf(IconsPacksApps)
    }
    private val iconPackManager get() = IconPackManager(context, packageManager, appsRepository)

    @Test
    fun loadIconPacks() {
        val result = iconPackManager.getAvailableIconPacks()
        assertEquals(IconPacks.size, result.size)
        assertEquals(IconPacks.keys, result.keys)
        val expectedValues = IconPacks.values.toList()
        val resultValues = result.values.toList()
        assertEquals(expectedValues[0].name, resultValues[0].name)
        assertEquals(expectedValues[0].packageName, resultValues[0].packageName)
        assertEquals(expectedValues[1].name, resultValues[1].name)
        assertEquals(expectedValues[1].packageName, resultValues[1].packageName)
    }

    companion object TestData {

        private val IconPacks = mapOf(
            "material.ui" to buildIconPack("material.ui", "MaterialUi"),
            "one.ui" to buildIconPack("one.ui", "OneUi"),
        )
        private val IconsPacksApps = listOf(
            AppModel(AppId("material.ui"), AppName("MaterialUi")),
            AppModel(AppId("one.ui"), AppName("OneUi")),
        )
        private fun buildIconPack(packageName: String, name: String): IconPackManager.IconPack =
            mockk {
                every { this@mockk.packageName } returns packageName
                every { this@mockk.name } returns name
            }
    }
}
