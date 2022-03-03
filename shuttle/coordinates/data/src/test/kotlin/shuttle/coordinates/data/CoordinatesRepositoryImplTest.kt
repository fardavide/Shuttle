package shuttle.coordinates.data

import app.cash.turbine.test
import arrow.core.left
import com.soywiz.klock.Time
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import shuttle.coordinates.data.datasource.LocationDataSource
import shuttle.coordinates.data.datasource.TimeDataSource
import shuttle.coordinates.domain.error.LocationNotAvailable
import shuttle.coordinates.domain.model.CoordinatesResult
import kotlin.test.Test
import kotlin.test.assertEquals

class CoordinatesRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()
    private val appScope = TestScope(context = dispatcher)
    private val locationDataSource: LocationDataSource = mockk {
        every { locationFlow } returns flowOf(CoordinatesResult.location)
    }
    private val timeDataSource: TimeDataSource = mockk {
        every { timeFlow } returns flowOf(CoordinatesResult.time)
    }
    private val repository = CoordinatesRepositoryImpl(
        appScope = appScope,
        locationDataSource = locationDataSource,
        timeDataSource = timeDataSource
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
            time = Time(hour = 4),
            location = LocationNotAvailable.left()
        )
    }
}
