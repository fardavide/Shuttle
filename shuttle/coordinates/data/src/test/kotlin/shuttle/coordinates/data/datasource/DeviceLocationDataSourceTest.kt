package shuttle.coordinates.data.datasource

import android.location.Location
import arrow.core.left
import arrow.core.right
import com.soywiz.klock.DateTime
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import shuttle.coordinates.domain.error.LocationError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.DurationUnit.MINUTES
import kotlin.time.toDuration

internal class DeviceLocationDataSourceTest {

    private val locationClient: ShuttleLocationClient = mockk()
    private val dateTimeSource: DateTimeDataSource = mockk {
        every { flow } returns flowOf(NowDateTime)
    }

    private val dataSource = DeviceLocationDataSource(
        freshLocationMinInterval = Interval.MinRefresh,
        locationClient = locationClient,
        locationExpiration = Interval.Expiration,
        dateTimeSource = dateTimeSource
    )

    @Test
    fun `do not fetch fresh location within min refresh time`() = runTest {
        // given
        coEvery { locationClient.getLastLocation() } returns buildLocation(dateTime = NowDateTime).right()

        // when
        dataSource.getLocation()

        // then
        coVerify { locationClient.getCurrentLocation() wasNot Called }
    }

    @Test
    fun `fetch fresh location if min refresh time is passed`() = runTest {
        // given
        coEvery { locationClient.getCurrentLocation() } returns buildLocation(dateTime = NowDateTime).right()
        coEvery { locationClient.getLastLocation() } returns buildLocation(dateTime = DateTime(0)).right()

        // when
        dataSource.getLocation()

        // then
        coVerify(exactly = 1) { locationClient.getCurrentLocation() }
    }

    @Test
    fun `if current location is not available, returns last if not expired`() = runTest {
        // given
        val expected = buildLocation(dateTime = NowDateTime).right()
        coEvery { locationClient.getLastLocation() } returns expected
        coEvery { locationClient.getCurrentLocation() } returns LocationError.Timeout.left()

        // when
        val result = dataSource.getLocation()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `if current location is not available and last is expired, returns expired`() = runTest {
        // given
        val expected = LocationError.ExpiredLocation.left()
        coEvery { locationClient.getLastLocation() } returns expected
        coEvery { locationClient.getCurrentLocation() } returns LocationError.Timeout.left()

        // when
        val result = dataSource.getLocation()

        // then
        assertEquals(expected, result)
    }

    companion object TestData {

        val NowDateTime = DateTime(987_654_321)

        fun buildLocation(dateTime: DateTime): Location = mockk {
            every { time } returns dateTime.unixMillisLong
        }

        object Interval {

            val Expiration = 20.toDuration(MINUTES)
            val MinRefresh = 2.toDuration(MINUTES)
        }
    }
}
