package shuttle.payments.presentation.util

import android.app.Activity
import arrow.core.Either
import arrow.core.raise.either
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import org.koin.core.annotation.Factory
import shuttle.payments.domain.model.PaymentError
import shuttle.payments.domain.model.Product
import shuttle.payments.domain.model.PurchaseSuccess
import shuttle.payments.domain.model.toSuccessOrErrorReason
import shuttle.payments.domain.usecase.GetProductDetails

@Factory
class LaunchPurchaseFlow(
    private val billingClient: BillingClient,
    private val getProductDetails: GetProductDetails
) {

    suspend operator fun invoke(activity: Activity, product: Product): Either<PaymentError, PurchaseSuccess> =
        either {
            val productDetails = getProductDetails(product).bind()

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
                .toSuccessOrErrorReason()
                .mapLeft { PaymentError.PurchaseFlow(it) }
                .bind()
        }
}
