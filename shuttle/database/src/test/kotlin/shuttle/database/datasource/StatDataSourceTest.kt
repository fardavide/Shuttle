package shuttle.database.datasource

import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import shuttle.database.testdata.TestData.ExactTime
import shuttle.database.testdata.TestData.FirstAppId
import shuttle.database.testdata.TestData.GeoHash
import shuttle.database.testutil.DatabaseTest

class StatDataSourceTest : DatabaseTest() {

    private val dispatcher = StandardTestDispatcher()
    private val queries = spyk(database.statQueries)
    private val dataSource = StatDataSourceImpl(
        statQueries = queries,
        ioDispatcher = dispatcher
    )

    @Test
    fun `increment counter when app was not stored`() = runTest(dispatcher) {
        // given
        val expectedCount = 1L

        // when
        dataSource.incrementCounter(FirstAppId, GeoHash, ExactTime)

        // then
        verify { queries.insertLocationStat(FirstAppId, GeoHash, expectedCount) }
    }

    @Test
    fun `increment counter when app was already stored`() = runTest(dispatcher) {
        // given
        val expectedCount = 2L

        // when
        dataSource.incrementCounter(FirstAppId, GeoHash, ExactTime)
        dataSource.incrementCounter(FirstAppId, GeoHash, ExactTime)

        // then
        verify { queries.insertLocationStat(FirstAppId, GeoHash, expectedCount) }
    }
}
