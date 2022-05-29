package shuttle.payments.presentation

import org.koin.dsl.module
import shuttle.payments.presentation.util.LaunchPurchaseFlow

val paymentsPresentationModule = module {

    factory { LaunchPurchaseFlow(billingClient = get(), getProductDetails = get()) }
}
