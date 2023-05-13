package shuttle.utils.kotlin

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named

const val ComputationDispatcher = "Computation dispatcher"
const val IoDispatcher = "IO dispatcher"

@Module
internal class DispatchersModule {

    @Factory
    @Named(ComputationDispatcher)
    fun computationDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Factory
    @Named(IoDispatcher)
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
