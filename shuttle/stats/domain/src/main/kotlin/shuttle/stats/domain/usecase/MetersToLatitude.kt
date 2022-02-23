package shuttle.stats.domain.usecase

class MetersToLatitude {

    operator fun invoke(meters: Int): Double =
        meters / 111_111.00
}
