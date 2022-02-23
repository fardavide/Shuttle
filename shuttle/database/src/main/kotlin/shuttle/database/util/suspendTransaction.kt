package shuttle.database.util

import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

suspend fun <T : Transacter> T.suspendTransaction(dispatcher: CoroutineDispatcher, block: T.() -> Unit) {
    withContext(dispatcher) {
        transaction { block() }
    }
}
