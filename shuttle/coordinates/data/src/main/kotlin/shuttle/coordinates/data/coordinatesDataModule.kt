package shuttle.coordinates.data

import android.content.Context
import com.google.android.gms.location.LocationServices
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import shuttle.coordinates.data.datasource.DateTimeDataSource
import shuttle.coordinates.data.datasource.DeviceLocationDataSource
import shuttle.coordinates.data.mapper.GeoHashMapper
import shuttle.coordinates.data.worker.RefreshLocationWorker
import shuttle.coordinates.domain.CoordinatesRepository
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val coordinatesDataModule = module {

    single<CoordinatesRepository> {
        CoordinatesRepositoryImpl(
            appScope = get(),
            deviceLocationDataSource = get(),
            geoHashMapper = get(),
            lastLocationDataSource = get(),
            refreshLocationWorkerScheduler = get(),
            dateTimeDataSource = get()
        )
    }
    factory { DateTimeDataSource(refreshInterval = TimeRefreshInterval) }
    factory { GeoHashMapper() }
    factory {
        DeviceLocationDataSource(fusedLocationClient = get(), locationExpiration = LocationMaxRefreshInterval)
    }
    factory { LocationServices.getFusedLocationProviderClient(get<Context>()) }
    worker {
        RefreshLocationWorker(
            appContext = get(),
            params = get(),
            coordinatesRepository = get(),
            logger = get()
        )
    }
    factory {
        RefreshLocationWorker.Scheduler(
            workManager = get(),
            every = LocationDefaultRefreshInterval,
            flex = LocationMaxRefreshInterval
        )
    }
}

private val LocationDefaultRefreshInterval = 12.toDuration(DurationUnit.MINUTES)
private val LocationMaxRefreshInterval = 20.toDuration(DurationUnit.MINUTES)
private val TimeRefreshInterval = 1.toDuration(DurationUnit.MINUTES)
