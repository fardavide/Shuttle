package shuttle.payments.domain.model

sealed interface PurchaseErrorReason {

    val debugMessage: String

    data class BillingUnavailable(override val debugMessage: String): PurchaseErrorReason
    data class DeveloperError(override val debugMessage: String): PurchaseErrorReason
    data class GenericError(override val debugMessage: String): PurchaseErrorReason
    data class ItemAlreadyOwned(override val debugMessage: String): PurchaseErrorReason
    data class ItemNotOwned(override val debugMessage: String): PurchaseErrorReason
    data class ItemUnavailable(override val debugMessage: String): PurchaseErrorReason
    data class FeatureNotSupported(override val debugMessage: String): PurchaseErrorReason
    data class ServiceDisconnected(override val debugMessage: String): PurchaseErrorReason
    data class ServiceTimeout(override val debugMessage: String): PurchaseErrorReason
    data class ServiceUnavailable(override val debugMessage: String): PurchaseErrorReason
    data class UserCanceled(override val debugMessage: String): PurchaseErrorReason

    data class ErrorCodeNotFoundError(val errorCode: Int, override val debugMessage: String) : PurchaseErrorReason
}
