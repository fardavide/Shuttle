package shuttle.coordinates.data

import app.cash.turbine.test
import arrow.core.left
import com.soywiz.klock.DateTime
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import shuttle.coordinates.data.datasource.DateTimeDataSource
import shuttle.coordinates.data.mapper.GeoHashMapper
import shuttle.coordinates.domain.error.LocationNotAvailable
import shuttle.coordinates.domain.model.CoordinatesResult
import shuttle.database.datasource.LastLocationDataSource
import kotlin.test.Test
import kotlin.test.assertEquals

class CoordinatesRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()
    private val appScope = TestScope(context = dispatcher)
    private val lastLocationDataSource: LastLocationDataSource = mockk {
        every { find() } returns flowOf(null)
    }
    private val dateTimeDataSource: DateTimeDataSource = mockk {
        every { flow } returns flowOf(CoordinatesResult.dateTime)
    }
    private val repository = CoordinatesRepositoryImpl(
        appScope = appScope,
        deviceLocationDataSource = mockk(relaxUnitFun = true),
        geoHashMapper = GeoHashMapper(),
        lastLocationDataSource = lastLocationDataSource,
        refreshLocationWorkerScheduler = mockk(relaxUnitFun = true),
        dateTimeDataSource = dateTimeDataSource
    )

    @Test
    fun `correctly emits for first observer`() = runTest(dispatcher) {
        // given
        val expected = CoordinatesResult

        repository.observeCurrentCoordinates().test {
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `every observer receives the last emitted item immediately`() = runTest(dispatcher) {
        // given
        val expected = CoordinatesResult

        repository.observeCurrentCoordinates().test {
            assertEquals(expected, awaitItem())
        }
        repository.observeCurrentCoordinates().test {
            assertEquals(expected, awaitItem())
        }
    }

    companion object TestData {

        private val CoordinatesResult = CoordinatesResult(
            location = LocationNotAvailable.left(),
            dateTime = DateTime.EPOCH
        )
    }
}
