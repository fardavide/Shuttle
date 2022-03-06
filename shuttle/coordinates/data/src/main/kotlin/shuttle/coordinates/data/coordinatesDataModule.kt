package shuttle.coordinates.data

import android.content.Context
import com.google.android.gms.location.LocationServices
import org.koin.dsl.module
import shuttle.coordinates.data.datasource.LocationDataSource
import shuttle.coordinates.data.datasource.TimeDataSource
import shuttle.coordinates.data.mapper.GeoHashMapper
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
    factory { GeoHashMapper() }
    factory {
        LocationDataSource(
            context = get(),
            geoHashMapper = get(),
            fusedLocationClient = get(),
            minRefreshInterval = LocationMinRefreshInterval,
            defaultRefreshInterval = LocationDefaultRefreshInterval,
            maxRefreshInterval = LocationMaxRefreshInterval,
        )
    }
    factory { LocationServices.getFusedLocationProviderClient(get<Context>()) }
    factory { TimeDataSource(refreshInterval = TimeRefreshInterval) }
}

private val LocationMinRefreshInterval = 3.toDuration(DurationUnit.MINUTES)
private val LocationDefaultRefreshInterval = 12.toDuration(DurationUnit.MINUTES)
private val LocationMaxRefreshInterval = 20.toDuration(DurationUnit.MINUTES)
private val TimeRefreshInterval = 1.toDuration(DurationUnit.MINUTES)
