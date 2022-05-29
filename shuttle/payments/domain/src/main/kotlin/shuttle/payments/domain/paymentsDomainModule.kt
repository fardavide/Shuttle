package shuttle.payments.domain

import org.koin.dsl.module
import shuttle.payments.domain.usecase.GetProductDetails
import shuttle.payments.domain.usecase.GetProductPrice

val paymentsDomainModule = module {

    factory { GetProductDetails(repository = get()) }
    factory { GetProductPrice(repository = get()) }
}
