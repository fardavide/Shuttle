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

        groupedByAppId.sortedByDescending { (_, stats) ->
            val weight = stats.sumOf { stat ->
                val byDays = 100 - (currentDayAsDatabaseData - stat.date).dayNumber
                val byLocation = if (currentCoordinates.location equals stat.geoHash) 10 else 1
                byDays * byLocation
            }
            weight - stats.size
        }.map { AppId(it.first.value) }
    }
}

private infix fun Either<LocationNotAvailable, GeoHash>.equals(other: DatabaseGeoHash?) =
    orNull()?.value == other?.value

