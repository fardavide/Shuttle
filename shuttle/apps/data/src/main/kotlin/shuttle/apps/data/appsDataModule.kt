package shuttle.apps.data

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import shuttle.apps.domain.AppsRepository

val appsDataModule = module {

    factory<AppsRepository> {
        AppsRepositoryImpl(
            dataSource = get(),
            packageManager = get(),
            ioDispatcher = Dispatchers.IO,
            isBlacklisted = get()
        )
    }
}
