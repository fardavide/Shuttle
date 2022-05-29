package shuttle.settings.presentation.viewmodel

import android.app.Activity
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import kotlinx.coroutines.launch
import shuttle.design.util.Effect
import shuttle.payments.domain.model.PaymentError
import shuttle.payments.domain.model.Product
import shuttle.payments.domain.model.PurchaseSuccess
import shuttle.payments.presentation.util.LaunchPurchaseFlow
import shuttle.settings.presentation.viewmodel.AboutViewModel.Action
import shuttle.settings.presentation.viewmodel.AboutViewModel.State
import shuttle.util.android.viewmodel.ShuttleViewModel

class AboutViewModel(
    private val launchPurchaseFlow: LaunchPurchaseFlow
) : ShuttleViewModel<Action, State>(initialState = State.Idle) {

    override fun submit(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.LaunchPurchase -> onLaunchPurchase(action.activity, action.product)
            }
        }
    }

    private suspend fun onLaunchPurchase(activity: Activity, product: Product) {
        val result = launchPurchaseFlow(activity, product)
        emit(State.Data(Effect.of(result)))
    }

    sealed interface Action {

        data class LaunchPurchase(val activity: Activity, val product: Product): Action
    }

    sealed interface State {

        data class Data(val purchaseResult: Effect<Either<PaymentError, PurchaseSuccess>>): State

        companion object {

            val Idle = Data(Effect.empty())
        }
    }
}
