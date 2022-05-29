package shuttle.payments.domain.model

sealed interface PaymentError {

    val reason: PaymentErrorReason

    object Connect : PaymentError {

        override val reason = PaymentErrorReason.ServiceDisconnected("unknown")
    }
    data class GetProduct(override val reason: PaymentErrorReason) : PaymentError
    data class PurchaseFlow(override val reason: PaymentErrorReason) : PaymentError
}

