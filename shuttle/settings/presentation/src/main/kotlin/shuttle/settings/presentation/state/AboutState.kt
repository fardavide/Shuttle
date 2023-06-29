package shuttle.settings.presentation.state

import arrow.core.Either
import shuttle.design.util.Effect
import shuttle.payments.domain.model.PaymentError
import shuttle.payments.domain.model.PurchaseSuccess

sealed interface AboutState {

    data class Data(
        val smallProductFormattedPrice: String,
        val largeProductFormattedPrice: String,
        val purchaseResult: Effect<Either<PaymentError, PurchaseSuccess>>
    ) : AboutState

    object Loading : AboutState

    object Error : AboutState
}
