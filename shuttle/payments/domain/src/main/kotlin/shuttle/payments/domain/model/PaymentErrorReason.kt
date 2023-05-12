package shuttle.payments.domain.model

import arrow.core.left
import arrow.core.right
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingResult

sealed interface PaymentErrorReason {

    val debugMessage: String

    data class BillingUnavailable(override val debugMessage: String) : PaymentErrorReason
    data class DeveloperError(override val debugMessage: String) : PaymentErrorReason
    data class GenericError(override val debugMessage: String) : PaymentErrorReason
    data class ItemAlreadyOwned(override val debugMessage: String) : PaymentErrorReason
    data class ItemNotOwned(override val debugMessage: String) : PaymentErrorReason
    data class ItemUnavailable(override val debugMessage: String) : PaymentErrorReason
    data class NetworkError(override val debugMessage: String) : PaymentErrorReason
    data class FeatureNotSupported(override val debugMessage: String) : PaymentErrorReason
    data class ServiceDisconnected(override val debugMessage: String) : PaymentErrorReason
    data class ServiceUnavailable(override val debugMessage: String) : PaymentErrorReason
    data class UserCanceled(override val debugMessage: String) : PaymentErrorReason

    data class ErrorCodeNotFoundError(val errorCode: Int, override val debugMessage: String) : PaymentErrorReason
}

fun BillingResult.toSuccessOrErrorReason() = when (responseCode) {
    BillingResponseCode.OK -> PurchaseSuccess.right()
    BillingResponseCode.BILLING_UNAVAILABLE -> PaymentErrorReason.BillingUnavailable(debugMessage).left()
    BillingResponseCode.DEVELOPER_ERROR -> PaymentErrorReason.DeveloperError(debugMessage).left()
    BillingResponseCode.ERROR -> PaymentErrorReason.GenericError(debugMessage).left()
    BillingResponseCode.FEATURE_NOT_SUPPORTED -> PaymentErrorReason.FeatureNotSupported(debugMessage).left()
    BillingResponseCode.ITEM_ALREADY_OWNED -> PaymentErrorReason.ItemAlreadyOwned(debugMessage).left()
    BillingResponseCode.ITEM_NOT_OWNED -> PaymentErrorReason.ItemNotOwned(debugMessage).left()
    BillingResponseCode.ITEM_UNAVAILABLE -> PaymentErrorReason.ItemUnavailable(debugMessage).left()
    BillingResponseCode.NETWORK_ERROR -> PaymentErrorReason.NetworkError(debugMessage).left()
    BillingResponseCode.SERVICE_DISCONNECTED -> PaymentErrorReason.ServiceDisconnected(debugMessage).left()
    BillingResponseCode.SERVICE_UNAVAILABLE -> PaymentErrorReason.ServiceUnavailable(debugMessage).left()
    BillingResponseCode.USER_CANCELED -> PaymentErrorReason.UserCanceled(debugMessage).left()
    else -> PaymentErrorReason.ErrorCodeNotFoundError(responseCode, debugMessage).left()
}
