package shuttle.payments.data

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.queryProductDetails
import shuttle.payments.data.util.connect
import shuttle.payments.domain.PaymentsRepository
import shuttle.payments.domain.model.PaymentError
import shuttle.payments.domain.model.PaymentErrorReason
import shuttle.payments.domain.model.Product
import shuttle.payments.domain.model.ProductPrice
import shuttle.payments.domain.model.toSuccessOrErrorReason
import shuttle.payments.presentation.mapper.QueryProductDetailsParamsMapper

internal class PaymentsRepositoryImpl(
    private val billingClient: BillingClient,
    private val paramsMapper: QueryProductDetailsParamsMapper
): PaymentsRepository {

    override suspend fun getPrice(product: Product): Either<PaymentError.GetProduct, ProductPrice> =
        getProductDetails(product).flatMap { productDetails ->
            val offerDetails = productDetails.oneTimePurchaseOfferDetails
            if (offerDetails == null) {
                val debugMessage = "null 'oneTimePurchaseOfferDetails'"
                val reason = PaymentErrorReason.GenericError(debugMessage)
                PaymentError.GetProduct(reason).left()
            } else {
                ProductPrice(offerDetails.formattedPrice).right()
            }
        }

    override suspend fun getProductDetails(product: Product): Either<PaymentError.GetProduct, ProductDetails> =
        either {
            billingClient.connect()

            val queryParams = paramsMapper.toQueryProductDetailsParams(product)

            billingClient.queryProductDetails(queryParams)
                .mapWith({ it.billingResult }) { it.productDetailsList }
                .mapLeft { PaymentError.GetProduct(it) }
                .bind()
                .first()
        }

    private fun <A, B : Any> A.mapWith(
        getBillingResult: (A) -> BillingResult,
        f: (A) -> B?
    ): Either<PaymentErrorReason, B> =
        getBillingResult(this)
            .toSuccessOrErrorReason()
            .map { checkNotNull(f(this)) }
}
