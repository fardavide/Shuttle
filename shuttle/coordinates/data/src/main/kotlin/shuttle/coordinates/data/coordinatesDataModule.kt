package shuttle.coordinates.data

import android.content.Context
import com.google.android.gms.location.LocationServices
import org.koin.dsl.module
import shuttle.coordinates.data.datasource.LocationDataSource
import shuttle.coordinates.data.datasource.TimeDataSource
import shuttle.coordinates.domain.CoordinatesRepository
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val coordinatesDataModule = module {

    single<CoordinatesRepository> {
        CoordinatesRepositoryImpl(
            appScope = get(),
            locationDataSource = get(),
            timeDataSource = get()
        )
    }
    factory {
        LocationDataSource(
            backoffInterval = LocationBackoffInterval,
            fusedLocationClient = get(),
            refreshInterval = LocationRefreshInterval
        )
    }
    factory { LocationServices.getFusedLocationProviderClient(get<Context>()) }
    factory { TimeDataSource(refreshInterval = TimeRefreshInterval) }
}

private val LocationBackoffInterval = 10.toDuration(DurationUnit.SECONDS)
private val LocationRefreshInterval = 10.toDuration(DurationUnit.MINUTES)
private val TimeRefreshInterval = 1.toDuration(DurationUnit.MINUTES)
