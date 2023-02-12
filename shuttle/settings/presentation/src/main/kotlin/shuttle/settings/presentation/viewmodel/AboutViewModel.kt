package shuttle.settings.presentation.viewmodel

import android.app.Activity
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.getOrElse
import co.touchlab.kermit.Logger
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import shuttle.design.util.Effect
import shuttle.payments.domain.model.PaymentError
import shuttle.payments.domain.model.Product
import shuttle.payments.domain.model.PurchaseSuccess
import shuttle.payments.domain.usecase.GetProductPrice
import shuttle.payments.presentation.util.LaunchPurchaseFlow
import shuttle.settings.presentation.viewmodel.AboutViewModel.Action
import shuttle.settings.presentation.viewmodel.AboutViewModel.State
import shuttle.util.android.viewmodel.ShuttleViewModel

class AboutViewModel(
    private val getProductPrice: GetProductPrice,
    private val launchPurchaseFlow: LaunchPurchaseFlow,
    logger: Logger
) : ShuttleViewModel<Action, State>(initialState = State.Loading) {

    init {
        viewModelScope.launch {
            val smallProductPriceDeferred = async { getProductPrice(Product.Small) }
            val largeProductPriceDeferred = async { getProductPrice(Product.Large) }

            val state = either<PaymentError, State.Data> {
                State.Data(
                    smallProductFormattedPrice = smallProductPriceDeferred.await().bind().formatted,
                    largeProductFormattedPrice = largeProductPriceDeferred.await().bind().formatted,
                    purchaseResult = Effect.empty()
                )
            }.getOrElse {
                logger.e(it.toString())
                State.Error
            }
            emit(state)
        }
    }

    override fun submit(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.LaunchPurchase -> onLaunchPurchase(action.activity, action.product)
            }
        }
    }

    private suspend fun onLaunchPurchase(activity: Activity, product: Product) {
        val result = launchPurchaseFlow(activity, product)
        val prevState = state.value as State.Data
        val newState = prevState.copy(purchaseResult = Effect.of(result))
        emit(newState)
    }

    sealed interface Action {

        data class LaunchPurchase(val activity: Activity, val product: Product) : Action
    }

    sealed interface State {

        data class Data(
            val smallProductFormattedPrice: String,
            val largeProductFormattedPrice: String,
            val purchaseResult: Effect<Either<PaymentError, PurchaseSuccess>>
        ) : State

        object Loading : State

        object Error : State
    }
}
