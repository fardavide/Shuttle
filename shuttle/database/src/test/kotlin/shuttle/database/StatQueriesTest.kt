package shuttle.database

import shuttle.database.testutil.DatabaseTest
import kotlin.test.Test

class StatQueriesTest : DatabaseTest() {

    private val queries = database.statQueries

    @Test
    fun test() {
        println(queries)
    }
}
