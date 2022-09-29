package shuttle.stats.data.usecase

import arrow.core.Either
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import shuttle.apps.domain.model.AppId
import shuttle.coordinates.domain.error.LocationNotAvailable
import shuttle.coordinates.domain.model.GeoHash
import shuttle.coordinates.domain.usecase.ObserveCurrentCoordinates
import shuttle.database.Stat
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseDate
import shuttle.database.model.DatabaseGeoHash
import shuttle.database.model.DatabaseTime
import shuttle.stats.data.mapper.DatabaseDateAndTimeMapper
import kotlin.math.pow

internal class SortAppStats(
    private val computationDispatcher: CoroutineDispatcher,
    private val databaseDateAndTimeMapper: DatabaseDateAndTimeMapper,
    private val observeCurrentCoordinates: ObserveCurrentCoordinates
) {

    suspend operator fun invoke(
        stats: Collection<Stat>,
        location: Either<LocationNotAvailable, DatabaseGeoHash>,
        date: DatabaseDate,
        startTime: DatabaseTime,
        endTime: DatabaseTime
    ): List<AppId> = withContext(computationDispatcher) {
        val currentLocation = location.fold(
            ifLeft = { DatabaseGeoHash("") },
            ifRight = { geoHash -> DatabaseGeoHash(geoHash.value) }
        )
        val currentTimeRange = startTime to endTime

        val byLocationAndTimeStats = stats.filter(byLocation = currentLocation, byTime = currentTimeRange)
        val byLocationOnlyStats = stats.filter(byLocation = currentLocation, byTimeNot = currentTimeRange)
        val byTimeOnlyStats = stats.filter(byLocationNot = currentLocation, byTime = currentTimeRange)
        val otherStats = stats.filter(byLocationNot = currentLocation, byTimeNot = currentTimeRange)

        val result = mutableListOf<AppId>()

        fun handle(stats: Collection<Stat>) {
            val sorted = stats.groupBy { stat -> stat.appId }.toList().sort(date) - result.toSet()
            result += sorted
        }

        handle(byLocationAndTimeStats)
        handle(byLocationOnlyStats)
        handle(byTimeOnlyStats)
        handle(otherStats)

        return@withContext result
    }

    private fun Collection<Stat>.filter(
        byLocation: DatabaseGeoHash? = null,
        byLocationNot: DatabaseGeoHash? = null,
        byTime: Pair<DatabaseTime, DatabaseTime>? = null,
        byTimeNot: Pair<DatabaseTime, DatabaseTime>? = null
    ): Collection<Stat> {
        check((byLocation != null && byLocationNot == null) || (byLocation == null && byLocationNot != null)) {
            "Either byLocation or byLocationNot must be not null"
        }
        check((byTime != null && byTimeNot == null) || (byTime == null && byTimeNot != null)) {
            "Either byTime or byTimeNot must be not null"
        }

        return asSequence()
            .filter { stat ->
                val filterByLocation = byLocation == null ||
                    byLocation == stat.geoHash
                val filterByTime = byTime == null ||
                    stat.time.minuteOfTheDay in byTime.first.minuteOfTheDay..byTime.second.minuteOfTheDay
                filterByLocation && filterByTime
            }
            .filterNot { stat ->
                val filterByLocationNot = byLocationNot != null &&
                    stat.geoHash == byLocationNot
                val filterByTimeNot = byTimeNot != null &&
                    stat.time.minuteOfTheDay in byTimeNot.first.minuteOfTheDay..byTimeNot.second.minuteOfTheDay
                filterByLocationNot && filterByTimeNot
            }
            .toList()
    }

    private fun Collection<Pair<DatabaseAppId, List<Stat>>>.sort(date: DatabaseDate): List<AppId> =
        asSequence()
            .sortedByDescending { (_, stats) ->
                stats.sumOf { stat -> DaysToKeep - (date.dayNumber - stat.date.dayNumber) }
            }
            .map { (appId, _) -> AppId(appId.value) }
            .toList()

    companion object {

        private const val DaysToKeep = 90
    }
}
