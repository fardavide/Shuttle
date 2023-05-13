package shuttle.payments.data.util

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import shuttle.payments.domain.model.PaymentError
import shuttle.payments.domain.model.PurchaseSuccess
import kotlin.coroutines.resume

private var connectingJob: Deferred<Either<PaymentError.Connect, PurchaseSuccess>>? = null

internal suspend inline fun BillingClient.connect(): Either<PaymentError.Connect, PurchaseSuccess> =
    coroutineScope {
        if (connectingJob != null) {
            connectingJob!!.await()
        } else {
            connectingJob = async { suspendConnect() }
            connectingJob!!.await()
        }
    }

private suspend inline fun BillingClient.suspendConnect(): Either<PaymentError.Connect, PurchaseSuccess> =
    suspendCancellableCoroutine { continuation ->
        val listener = object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                continuation.resume(PaymentError.Connect.left())
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                continuation.resume(PurchaseSuccess.right())
            }
        }
        continuation.invokeOnCancellation {}
        startConnection(listener)
    }
