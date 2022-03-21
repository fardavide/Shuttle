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
        val iconPack = iconPackManager.getAvailableIconPacks().values.first()
        iconPack.load()

        val result = iconPack.totalIcons
        assertEquals(0, result)
    }

    companion object TestData {

        object Alexis {
            const val packageName = "com.bandot.alexis.minimal.iconpack"
            const val name = "Alexis"
        }

        object Borealis {
            const val packageName = "com.unvoid.borealis"
            const val name = "Borealis"
        }

        object Darko {
            const val packageName = "com.darkopd.iconpack"
            const val name = "Darko"
        }

        private val IconPacks = mapOf(
            Alexis.packageName to buildIconPack(Alexis.packageName, Alexis.name),
            Borealis.packageName to buildIconPack(Borealis.packageName, Borealis.name),
            Darko.packageName to buildIconPack(Darko.packageName, Darko.name),
        )
        private val IconsPacksApps = listOf(
            AppModel(AppId(Alexis.packageName), AppName(Alexis.name)),
            AppModel(AppId(Borealis.packageName), AppName(Borealis.name)),
            AppModel(AppId(Darko.packageName), AppName(Darko.name)),
        )
        private fun buildIconPack(packageName: String, name: String): IconPackManager.IconPack =
            mockk {
                every { this@mockk.packageName } returns packageName
                every { this@mockk.name } returns name
            }
    }
}
