package shuttle.database.util

import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

suspend inline fun <T : Transacter> T.suspendTransaction(
    dispatcher: CoroutineDispatcher,
    crossinline block: T.() -> Unit
) {
    withContext(dispatcher) {
        transaction { block() }
    }
}
