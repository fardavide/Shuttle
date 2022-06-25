package shuttle.coordinates.data

import android.content.Context
import com.google.android.gms.location.LocationServices
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import shuttle.coordinates.data.datasource.DateTimeDataSource
import shuttle.coordinates.data.datasource.DeviceLocationDataSource
import shuttle.coordinates.data.datasource.ShuttleLocationClient
import shuttle.coordinates.data.mapper.GeoHashMapper
import shuttle.coordinates.data.worker.RefreshLocationWorker
import shuttle.coordinates.domain.CoordinatesRepository
import kotlin.time.DurationUnit.HOURS
import kotlin.time.DurationUnit.MINUTES
import kotlin.time.DurationUnit.SECONDS
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
    factory { DateTimeDataSource(refreshInterval = Interval.Time.Refresh) }
    factory { GeoHashMapper() }
    factory {
        DeviceLocationDataSource(
            dateTimeSource = get(),
            freshLocationMinInterval = Interval.Location.MinRefresh,
            locationClient = get(),
            locationExpiration = Interval.Location.Expiration
        )
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
            every = Interval.Location.Scheduler.Default,
            flex = Interval.Location.Scheduler.Flex
        )
    }
    factory {
        ShuttleLocationClient(
            freshLocationTimeout = Interval.Location.FetchTimeout,
            fusedLocationClient = get()
        )
    }
}

private object Interval {

    object Location {

        val Expiration = 20.toDuration(MINUTES)
        val FetchTimeout = 10.toDuration(SECONDS)
        val MinRefresh = 2.toDuration(MINUTES)

        object Scheduler {

            val Default = 15.toDuration(MINUTES)
            val Flex = 1.toDuration(HOURS)
        }
    }

    object Time {

        val Refresh = 1.toDuration(MINUTES)
    }
}
