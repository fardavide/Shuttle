package shuttle.coordinates.data

import android.content.Context
import com.google.android.gms.location.LocationServices
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@ComponentScan
class CoordinatesDataModule {

    @Factory
    fun fusedLocationProviderClient(context: Context) =
        LocationServices.getFusedLocationProviderClient(context)
}
