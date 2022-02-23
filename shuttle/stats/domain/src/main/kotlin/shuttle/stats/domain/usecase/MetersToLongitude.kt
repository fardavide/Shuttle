package shuttle.stats.domain.usecase

import kotlin.math.cos

class MetersToLongitude {

    operator fun invoke(meters: Int, latitude: Double): Double =
        meters / (111_111.00 / cos(latitude))
}
