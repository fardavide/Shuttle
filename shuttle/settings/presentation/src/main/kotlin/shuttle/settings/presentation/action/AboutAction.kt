package shuttle.settings.presentation.action

import android.app.Activity
import shuttle.payments.domain.model.Product

sealed interface AboutAction {

    data class LaunchPurchase(val activity: Activity, val product: Product) : AboutAction
}
