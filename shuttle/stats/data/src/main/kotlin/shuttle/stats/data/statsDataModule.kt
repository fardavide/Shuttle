package shuttle.stats.data

import org.koin.dsl.module
import shuttle.stats.domain.StatsRepository

val statsDataModule = module {

    factory<StatsRepository> { StatsRepositoryImpl(appsRepository = get(), statDataSource = get()) }
}
