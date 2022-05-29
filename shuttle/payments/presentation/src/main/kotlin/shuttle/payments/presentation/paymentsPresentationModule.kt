package shuttle.payments.presentation

import org.koin.dsl.module
import shuttle.payments.presentation.mapper.QueryProductDetailsParamsMapper

val paymentsPresentationModule = module {

    factory { QueryProductDetailsParamsMapper() }
}
