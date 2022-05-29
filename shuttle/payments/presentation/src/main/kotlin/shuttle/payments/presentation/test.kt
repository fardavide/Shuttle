package shuttle.payments.presentation

import android.app.Activity
import arrow.core.Either
import arrow.core.computations.either
import arrow.core.left
import arrow.core.right
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.queryProductDetails
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import shuttle.payments.domain.model.Product
import shuttle.payments.domain.model.PurchaseError
import shuttle.payments.domain.model.PurchaseErrorReason
import shuttle.payments.domain.model.PurchaseSuccess
import shuttle.payments.presentation.mapper.QueryProductDetailsParamsMapper
import kotlin.coroutines.resume

enum class BillingProduct(
    val queryParams: QueryProductDetailsParams
) {

    CoffeeProduct(
        queryParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("coffee")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            )
            .build()
    ),

    MakeUpProduct(
        queryParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("makeup")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            )
            .build()
    )
}

private class Getter : KoinComponent {

    val client: BillingClient by inject()
    val mapper: QueryProductDetailsParamsMapper by inject()
}

suspend fun launchPurchaseFlow(
    activity: Activity,
    product: Product
): Either<PurchaseError, PurchaseSuccess> = either {
    val getter = Getter()
    val billingClient = getter.client
    val mapper = getter.mapper

    billingClient.connect()

    val queryParams = mapper.toQueryProductDetailsParams(product)

    val productDetails = billingClient.queryProductDetails(queryParams)
        .mapWith({ it.billingResult }) { it.productDetailsList }
        .mapLeft { PurchaseError.GetProduct(it) }
        .bind()
        .first()

    val flowParams = BillingFlowParams.newBuilder()
        .setProductDetailsParamsList(
            listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetails)
                    .build()
            )
        )
        .build()

    billingClient.launchBillingFlow(activity, flowParams)
        .toSuccessOrPurchaseErrorReason()
        .mapLeft { PurchaseError.PurchaseFlow(it) }
        .bind()
}

private suspend inline fun BillingClient.connect(): Either<PurchaseError.Connect, PurchaseSuccess> =
    suspendCancellableCoroutine { continuation ->
        val listener = object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                continuation.resume(PurchaseError.Connect.left())
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                continuation.resume(PurchaseSuccess.right())
            }
        }
        continuation.invokeOnCancellation {}
        startConnection(listener)
    }

private fun <A, B : Any> A.mapWith(
    getBillingResult: (A) -> BillingResult,
    f: (A) -> B?
): Either<PurchaseErrorReason, B> =
    getBillingResult(this)
        .toSuccessOrPurchaseErrorReason()
        .map { checkNotNull(f(this)) }

private fun BillingResult.toSuccessOrPurchaseErrorReason() = when (responseCode) {
    BillingResponseCode.OK -> PurchaseSuccess.right()
    BillingResponseCode.BILLING_UNAVAILABLE -> PurchaseErrorReason.BillingUnavailable(debugMessage).left()
    BillingResponseCode.DEVELOPER_ERROR -> PurchaseErrorReason.DeveloperError(debugMessage).left()
    BillingResponseCode.ERROR -> PurchaseErrorReason.GenericError(debugMessage).left()
    BillingResponseCode.FEATURE_NOT_SUPPORTED -> PurchaseErrorReason.FeatureNotSupported(debugMessage).left()
    BillingResponseCode.ITEM_ALREADY_OWNED -> PurchaseErrorReason.ItemAlreadyOwned(debugMessage).left()
    BillingResponseCode.ITEM_NOT_OWNED -> PurchaseErrorReason.ItemNotOwned(debugMessage).left()
    BillingResponseCode.ITEM_UNAVAILABLE -> PurchaseErrorReason.ItemUnavailable(debugMessage).left()
    BillingResponseCode.SERVICE_DISCONNECTED -> PurchaseErrorReason.ServiceDisconnected(debugMessage).left()
    BillingResponseCode.SERVICE_TIMEOUT -> PurchaseErrorReason.ServiceTimeout(debugMessage).left()
    BillingResponseCode.SERVICE_UNAVAILABLE -> PurchaseErrorReason.ServiceUnavailable(debugMessage).left()
    BillingResponseCode.USER_CANCELED -> PurchaseErrorReason.UserCanceled(debugMessage).left()
    else -> PurchaseErrorReason.ErrorCodeNotFoundError(responseCode, debugMessage).left()
}
