package shuttle.payments.domain.usecase

import arrow.core.Either
import shuttle.payments.domain.PaymentsRepository
import shuttle.payments.domain.model.PaymentError
import shuttle.payments.domain.model.Product
import shuttle.payments.domain.model.ProductPrice

class GetProductPrice(
    private val repository: PaymentsRepository
) {

    suspend operator fun invoke(product: Product): Either<PaymentError.GetProduct, ProductPrice> =
        repository.getPrice(product)
}
