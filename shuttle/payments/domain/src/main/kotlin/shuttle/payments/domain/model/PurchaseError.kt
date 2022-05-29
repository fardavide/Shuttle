package shuttle.payments.domain.model

sealed interface PurchaseError {

    val reason: PurchaseErrorReason

    object Connect : PurchaseError {

        override val reason = PurchaseErrorReason.ServiceDisconnected("unknown")
    }
    data class GetProduct(override val reason: PurchaseErrorReason) : PurchaseError
    data class PurchaseFlow(override val reason: PurchaseErrorReason) : PurchaseError
}
