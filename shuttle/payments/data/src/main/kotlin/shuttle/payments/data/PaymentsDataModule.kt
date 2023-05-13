package shuttle.payments.data

import android.content.Context
import com.android.billingclient.api.BillingClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
class PaymentsDataModule {

    @Single
    fun billingClient(context: Context): BillingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases()
        .setListener { _, _ -> }
        .build()
}
