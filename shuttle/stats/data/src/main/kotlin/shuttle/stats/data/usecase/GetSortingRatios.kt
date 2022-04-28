package shuttle.stats.data.usecase

import shuttle.settings.domain.usecase.GetPrioritizeLocation
import shuttle.stats.data.model.GroupedDatabaseAppStats
import shuttle.stats.data.model.SortingRatios
import kotlin.math.pow

internal class GetSortingRatios(
    private val getPrioritizeLocation: GetPrioritizeLocation
) {

    suspend operator fun invoke(stats: GroupedDatabaseAppStats): SortingRatios {
        val locationPartialRatio =
            stats.byTimeCount.coerceAtLeast(1.0) / stats.byLocationCount.coerceAtLeast(1.0)

        return with(BaseRatios) {
            if (getPrioritizeLocation()) {
                SortingRatios(
                    byLocation = locationPartialRatio.pow(LocationPrioritized),
                    byTime = TimeWithLocationPrioritized
                )
            } else {
                SortingRatios(
                    byLocation = locationPartialRatio.pow(LocationNotPrioritized),
                    byTime = TimeWithLocationNotPrioritized
                )
            }
        }
    }

    private object BaseRatios {

        const val LocationNotPrioritized = 4.0
        const val LocationPrioritized = 99.0
        const val TimeWithLocationNotPrioritized = 1.0
        const val TimeWithLocationPrioritized = 0.5
    }
}
