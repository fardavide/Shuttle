package shuttle.coordinates.data

import android.content.Context
import com.google.android.gms.location.LocationServices
import org.koin.dsl.module
import shuttle.coordinates.domain.CoordinatesRepository

val coordinatesDataModule = module {

    factory<CoordinatesRepository> { CoordinatesRepositoryImpl(fusedLocationClient = get()) }
    factory { LocationServices.getFusedLocationProviderClient(get<Context>()) }
}