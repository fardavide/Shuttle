package shuttle.stats.data.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import shuttle.settings.domain.usecase.GetPrioritizeLocation
import shuttle.stats.data.model.GroupedDatabaseAppStats
import shuttle.stats.data.model.SortingRatios
import kotlin.test.Test

@RunWith(Parameterized::class)
internal class GetSortingRatiosTest(
    @Suppress("unused") testName: String,
    private val input: Params.Input,
    private val assertion: (SortingRatios) -> Boolean
) {

    private val getPrioritizeLocation: GetPrioritizeLocation = mockk {
        coEvery { this@mockk() } returns input.isPrioritizingLocation
    }
    private val getSortingRatios = GetSortingRatios(getPrioritizeLocation)

    @Test
    fun test() = runTest {
        val sortingRatios = getSortingRatios(input.stats)
        assert(assertion(sortingRatios)) {
            "SortingRatios: $sortingRatios"
        }
    }

    data class Params(
        val testName: String,
        val input: Input,
        val assertion: (SortingRatios) -> Boolean
    ) {

        data class Input(
            val isPrioritizingLocation: Boolean,
            val stats: GroupedDatabaseAppStats
        )
    }

    companion object TestData {

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = listOf(

            Params(
                testName = "location prioritized with 0 stats",
                Params.Input(
                    isPrioritizingLocation = true,
                    stats = buildStats(byLocationCount = 0, byTimeCount = 0)
                )
            ) { it.byLocation > it.byTime },

            Params(
                testName = "location prioritized with 3 by location and 30 by time",
                Params.Input(
                    isPrioritizingLocation = true,
                    stats = buildStats(byLocationCount = 3, byTimeCount = 30)
                )
            ) { it.byLocation > it.byTime },

            Params(
                testName = "location prioritized with 3 by location and 3 by time",
                Params.Input(
                    isPrioritizingLocation = true,
                    stats = buildStats(byLocationCount = 3, byTimeCount = 3)
                )
            ) { it.byLocation > it.byTime },

            Params(
                testName = "location prioritized with 300 by location and 300 by time",
                Params.Input(
                    isPrioritizingLocation = true,
                    stats = buildStats(byLocationCount = 300, byTimeCount = 300)
                )
            ) { it.byLocation > it.byTime },

            Params(
                testName = "location prioritized with 30 by location and 3 by time",
                Params.Input(
                    isPrioritizingLocation = true,
                    stats = buildStats(byLocationCount = 30, byTimeCount = 3)
                )
            ) { it.byLocation < it.byTime },

            Params(
                testName = "location not prioritized with 0 stats",
                Params.Input(
                    isPrioritizingLocation = false,
                    stats = buildStats(byLocationCount = 0, byTimeCount = 0)
                )
            ) { it.byLocation == it.byTime },

            Params(
                testName = "location not prioritized with 3 by location and 30 by time",
                Params.Input(
                    isPrioritizingLocation = false,
                    stats = buildStats(byLocationCount = 3, byTimeCount = 30)
                )
            ) { it.byLocation > it.byTime },

            Params(
                testName = "location not prioritized with 3 by location and 3 by time",
                Params.Input(
                    isPrioritizingLocation = false,
                    stats = buildStats(byLocationCount = 3, byTimeCount = 3)
                )
            ) { it.byLocation == it.byTime },

            Params(
                testName = "location not prioritized with 300 by location and 300 by time",
                Params.Input(
                    isPrioritizingLocation = false,
                    stats = buildStats(byLocationCount = 300, byTimeCount = 300)
                )
            ) { it.byLocation == it.byTime },

            Params(
                testName = "location not prioritized with 30 by location and 3 by time",
                Params.Input(
                    isPrioritizingLocation = false,
                    stats = buildStats(byLocationCount = 30, byTimeCount = 3)
                )
            ) { it.byLocation < it.byTime },

        ).map { arrayOf(it.testName, it.input, it.assertion) }

        private fun buildStats(
            byLocationCount: Int,
            byTimeCount: Int
        ) = GroupedDatabaseAppStats(
            byLocationCount = byLocationCount.toDouble(),
            byTimeCount = byTimeCount.toDouble(),
            groupedByAppId = emptyList()
        )
    }
}
