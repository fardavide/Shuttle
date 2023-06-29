package shuttle.settings.presentation.viewmodel

import android.app.Activity
import androidx.lifecycle.viewModelScope
import arrow.core.continuations.either
import arrow.core.getOrElse
import co.touchlab.kermit.Logger
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import shuttle.design.util.Effect
import shuttle.payments.domain.model.PaymentError
import shuttle.payments.domain.model.Product
import shuttle.payments.domain.usecase.GetProductPrice
import shuttle.payments.presentation.util.LaunchPurchaseFlow
import shuttle.settings.presentation.action.AboutAction
import shuttle.settings.presentation.state.AboutState
import shuttle.util.android.viewmodel.ShuttleViewModel

@KoinViewModel
class AboutViewModel(
    private val getProductPrice: GetProductPrice,
    private val launchPurchaseFlow: LaunchPurchaseFlow,
    logger: Logger
) : ShuttleViewModel<AboutAction, AboutState>(initialState = AboutState.Loading) {

    init {
        viewModelScope.launch {
            val smallProductPriceDeferred = async { getProductPrice(Product.Small) }
            val largeProductPriceDeferred = async { getProductPrice(Product.Large) }

            val state = either<PaymentError, AboutState.Data> {
                AboutState.Data(
                    smallProductFormattedPrice = smallProductPriceDeferred.await().bind().formatted,
                    largeProductFormattedPrice = largeProductPriceDeferred.await().bind().formatted,
                    purchaseResult = Effect.empty()
                )
            }.getOrElse {
                logger.e(it.toString())
                AboutState.Error
            }
            emit(state)
        }
    }

    override fun submit(action: AboutAction) {
        viewModelScope.launch {
            when (action) {
                is AboutAction.LaunchPurchase -> onLaunchPurchase(action.activity, action.product)
            }
        }
    }

    private suspend fun onLaunchPurchase(activity: Activity, product: Product) {
        val result = launchPurchaseFlow(activity, product)
        val prevState = state.value as AboutState.Data
        val newState = prevState.copy(purchaseResult = Effect.of(result))
        emit(newState)
    }

}
