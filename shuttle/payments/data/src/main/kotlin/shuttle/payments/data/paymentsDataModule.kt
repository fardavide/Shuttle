package shuttle.payments.data

import com.android.billingclient.api.BillingClient
import org.koin.dsl.module

val paymentsDataModule = module {

    single {
        BillingClient.newBuilder(get())
            .enablePendingPurchases()
            .setListener { _, _ -> }
            .build()
    }
}
