package shuttle.utils.kotlin

import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named

const val IoDispatcher = "IO dispatcher"

@Module
internal class DispatchersModule {

    @Factory
    @Named(IoDispatcher)
    fun ioDispatcher() = Dispatchers.IO
}
