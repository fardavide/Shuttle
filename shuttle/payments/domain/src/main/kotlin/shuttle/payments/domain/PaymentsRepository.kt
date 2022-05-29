package shuttle.payments.domain

import arrow.core.Either
import com.android.billingclient.api.ProductDetails
import shuttle.payments.domain.model.PaymentError
import shuttle.payments.domain.model.Product
import shuttle.payments.domain.model.ProductPrice

interface PaymentsRepository {

    suspend fun getPrice(product: Product): Either<PaymentError.GetProduct, ProductPrice>

    suspend fun getProductDetails(product: Product): Either<PaymentError.GetProduct, ProductDetails>
}
