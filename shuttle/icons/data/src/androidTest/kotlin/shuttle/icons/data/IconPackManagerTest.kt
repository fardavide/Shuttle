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
import kotlin.test.assertNotEquals

class IconPackManagerTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val packageManager = context.packageManager
    private val appsRepository: AppsRepository = mockk {
        every { observeInstalledIconPacks() } returns flowOf(IconsPacksApps)
    }
    private val iconPacksRepository = IconPacksRepositoryImpl(packageManager)
    private val iconPackManager get() = IconPackManager(
        appContext = context,
        packageManager = packageManager,
        appsRepository = appsRepository,
        iconPacksRepository = iconPacksRepository
    )

    @Test
    fun loadIconPacks() {
        val iconPack = iconPackManager.getAvailableIconPacks().values.first()
        iconPack.load()

        val result = iconPack.totalIcons
        assertNotEquals(0, result)
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

        private val IconsPacksApps = listOf(
            AppModel(AppId(Alexis.packageName), AppName(Alexis.name)),
            AppModel(AppId(Borealis.packageName), AppName(Borealis.name)),
            AppModel(AppId(Darko.packageName), AppName(Darko.name)),
        )
    }
}
