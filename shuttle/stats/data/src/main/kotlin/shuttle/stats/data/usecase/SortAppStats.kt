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
import shuttle.stats.data.mapper.DatabaseDateMapper

internal class SortAppStats(
    private val computationDispatcher: CoroutineDispatcher,
    private val databaseDateMapper: DatabaseDateMapper,
    private val observeCurrentCoordinates: ObserveCurrentCoordinates
) {

    suspend operator fun invoke(stats: Collection<Stat>): List<AppId> = withContext(computationDispatcher) {
        val currentCoordinates = observeCurrentCoordinates().first()
        val currentDayAsDatabaseData = databaseDateMapper.toDatabaseDate(currentCoordinates.dateTime)
        val groupedByAppId = stats.groupBy { it.appId }.toList()

        val (inLocation, outLocation) = groupedByAppId.partition { (_, stats) ->
            stats.any { stat -> currentCoordinates.location equals stat.geoHash }
        }

        inLocation.sortStats(currentDayAsDatabaseData) + outLocation.sortStats(currentDayAsDatabaseData)
    }

    private fun List<Pair<DatabaseAppId, List<Stat>>>.sortStats(currentDayAsDatabaseDate: DatabaseDate): List<AppId> =
        sortedByDescending { (_, stats) ->
            stats.sumOf { stat -> 100 - (currentDayAsDatabaseDate - stat.date).dayNumber }
        }.map { AppId(it.first.value) }
}

private infix fun Either<LocationNotAvailable, GeoHash>.equals(other: DatabaseGeoHash?) =
    orNull()?.value == other?.value

