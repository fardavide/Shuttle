package shuttle.predictions.domain

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import shuttle.predictions.domain.usecase.GetSuggestedApps
import shuttle.predictions.domain.usecase.SortAppsByStats

val predictionsDomainModule = module {

    factory { GetSuggestedApps(getAllInstalledApps = get(), getAllAppsStats = get(), sortAppsByStats = get()) }
    factory { SortAppsByStats(computationDispatcher = Dispatchers.Default) }

}
