package shuttle.apps.data

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import shuttle.apps.domain.AppsRepository

val appsDataModule = module {

    factory<AppsRepository> {
        AppsRepositoryImpl(
            packageManager = get(),
            statsDataSource = get(),
            ioDispatcher = Dispatchers.IO
        )
    }
}
