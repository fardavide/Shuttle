package shuttle.database

import kotlinx.coroutines.test.runTest
import shuttle.database.testdata.DatabaseAppIdSample
import shuttle.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AppBlacklistSettingQueriesTest : DatabaseTest() {

    private val queries = database.appBlacklistSettingQueries

    @Test
    fun `returns empty if no app is inserted`() = runTest {
        // given
        queries.insertAppBlacklistSetting(DatabaseAppIdSample.Chrome, isBlacklisted = false)
        queries.insertAppBlacklistSetting(DatabaseAppIdSample.CineScout, isBlacklisted = true)
        val expected = emptyList<FindAllAppsWithBlacklistSetting>()

        // when
        val result = queries.findAllAppsWithBlacklistSetting()
            .executeAsList()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `returns false if setting is not found`() = runTest {
        // given
        insertApps(1)
        val expected = listOf(
            FindAllAppsWithBlacklistSetting(DatabaseAppIdSample.Chrome, DatabaseAppIdSample.Chrome.value, 0)
        )

        // when
        val result = queries.findAllAppsWithBlacklistSetting()
            .executeAsList()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `returns false if setting is false`() = runTest {
        // given
        insertApps(1)
        queries.insertAppBlacklistSetting(DatabaseAppIdSample.Chrome, false)
        val expected = listOf(
            FindAllAppsWithBlacklistSetting(DatabaseAppIdSample.Chrome, DatabaseAppIdSample.Chrome.value, 0)
        )

        // when
        val result = queries.findAllAppsWithBlacklistSetting()
            .executeAsList()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `returns true if setting is true`() = runTest {
        // given
        insertApps(1)
        queries.insertAppBlacklistSetting(DatabaseAppIdSample.Chrome, true)
        val expected = listOf(
            FindAllAppsWithBlacklistSetting(DatabaseAppIdSample.Chrome, DatabaseAppIdSample.Chrome.value, 1)
        )

        // when
        val result = queries.findAllAppsWithBlacklistSetting()
            .executeAsList()

        // then
        assertEquals(expected, result)
    }
}
