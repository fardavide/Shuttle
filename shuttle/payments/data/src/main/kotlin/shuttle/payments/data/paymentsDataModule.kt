package shuttle.payments.data

import com.android.billingclient.api.BillingClient
import org.koin.dsl.module
import shuttle.payments.domain.PaymentsRepository
import shuttle.payments.presentation.mapper.QueryProductDetailsParamsMapper

val paymentsDataModule = module {

    single {
        BillingClient.newBuilder(get())
            .enablePendingPurchases()
            .setListener { _, _ -> }
            .build()
    }
    factory<PaymentsRepository> { PaymentsRepositoryImpl(billingClient = get(), paramsMapper = get()) }
    factory { QueryProductDetailsParamsMapper() }
}
