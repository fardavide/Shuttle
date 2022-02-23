package shuttle.predictions.domain.usecase

internal class MetersToLatitude {

    operator fun invoke(meters: Int): Double =
        meters / 111_111.00
}
