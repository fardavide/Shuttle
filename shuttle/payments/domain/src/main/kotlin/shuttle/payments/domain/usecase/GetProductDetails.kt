package shuttle.payments.domain.usecase

import arrow.core.Either
import com.android.billingclient.api.ProductDetails
import org.koin.core.annotation.Factory
import shuttle.payments.domain.PaymentsRepository
import shuttle.payments.domain.model.PaymentError
import shuttle.payments.domain.model.Product

@Factory
class GetProductDetails(
    private val repository: PaymentsRepository
) {

    suspend operator fun invoke(product: Product): Either<PaymentError.GetProduct, ProductDetails> =
        repository.getProductDetails(product)
}
