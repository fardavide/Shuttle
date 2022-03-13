package shuttle.permissions.mapper

import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import shuttle.permissions.model.LocationPermissionsState
import shuttle.permissions.model.PermissionItem
import shuttle.permissions.model.PermissionItemsUiModel
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
internal class PermissionItemsUiModelMapperTest(
    private val parameters: Parameters
) {

    private val input = parameters.input
    private val expected = parameters.expected
    private val mapper = PermissionItemsUiModelMapper()

    @Test
    fun test() {
        val result = mapper.toUiModel(input.locationPermissionsState, input.isAccessibilityServiceEnabled)
        assertEquals(expected, result)
    }

    data class Input(
        val locationPermissionsState: LocationPermissionsState,
        val isAccessibilityServiceEnabled: Boolean
    )

    data class Parameters(
        val testName: String,
        val input: Input,
        val expected: PermissionItemsUiModel
    ) {

        override fun toString() = testName
    }

    companion object TestData {

        @JvmStatic
        @Parameterized.Parameters(name = "{index}: {0}")
        fun data() = listOf(
            Parameters(
                testName = "All granted when Location and Accessibility are all granted",
                Input(LocationPermissionsState.AllGranted, isAccessibilityServiceEnabled = true),
                expected = PermissionItemsUiModel(
                    PermissionItem.Location.Coarse.Granted,
                    PermissionItem.Location.Fine.Granted,
                    PermissionItem.Location.Background.Granted,
                    PermissionItem.Accessibility.Granted,
                )
            )
        ).map { arrayOf(it) }
    }
}
