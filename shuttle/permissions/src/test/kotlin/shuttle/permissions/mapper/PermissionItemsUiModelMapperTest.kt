package shuttle.permissions.mapper

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import io.mockk.every
import io.mockk.mockk
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import shuttle.permissions.model.PermissionItem
import shuttle.permissions.model.PermissionItemsUiModel
import shuttle.util.android.IsAndroidQ
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
@OptIn(ExperimentalPermissionsApi::class)
internal class PermissionItemsUiModelMapperTest(
    parameters: Parameters
) {

    private val input = parameters.input
    private val expected = parameters.expected

    private val isAndroidQ: IsAndroidQ = mockk {
        every { this@mockk() } returns input.isAndroidQ
    }
    private val mapper = PermissionItemsUiModelMapper(isAndroidQ)

    @Test
    fun test() {
        val result = mapper.toUiModel(input.permissionsState, input.isAccessibilityServiceEnabled)
        assertEquals(expected, result)
    }

    data class Input(
        val permissionsState: MultiplePermissionsState,
        val isAccessibilityServiceEnabled: Boolean,
        val isAndroidQ: Boolean = true
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
                testName = "All granted",
                Input(
                    buildMultiplePermissionsState(
                        hasCoarseLocation = true,
                        hasFineLocation = true,
                        hasBackgroundLocation = true
                    ), isAccessibilityServiceEnabled = true
                ),
                expected = PermissionItemsUiModel(
                    PermissionItem.Location.Coarse.Granted,
                    PermissionItem.Location.Fine.Granted,
                    PermissionItem.Location.Background.Granted,
                    PermissionItem.Accessibility.Granted,
                )
            ),

            Parameters(
                testName = "None granted",
                input = Input(
                    buildMultiplePermissionsState(
                        hasCoarseLocation = false,
                        hasFineLocation = false,
                        hasBackgroundLocation = false
                    ), isAccessibilityServiceEnabled = false
                ),
                expected = PermissionItemsUiModel(
                    PermissionItem.Location.Coarse.NotGranted,
                    PermissionItem.Location.Fine.NotGranted,
                    PermissionItem.Location.Background.NotGranted,
                    PermissionItem.Accessibility.NotGranted,
                )
            ),

            Parameters(
                testName = "Only accessibility not granted",
                input = Input(
                    buildMultiplePermissionsState(
                        hasCoarseLocation = true,
                        hasFineLocation = true,
                        hasBackgroundLocation = true
                    ), isAccessibilityServiceEnabled = false
                ),
                expected = PermissionItemsUiModel(
                    PermissionItem.Location.Coarse.Granted,
                    PermissionItem.Location.Fine.Granted,
                    PermissionItem.Location.Background.Granted,
                    PermissionItem.Accessibility.NotGranted,
                )
            ),

        ).map { arrayOf(it) }

        private fun buildMultiplePermissionsState(
            hasCoarseLocation: Boolean,
            hasFineLocation: Boolean,
            hasBackgroundLocation: Boolean
        ): MultiplePermissionsState = mockk()
    }
}
