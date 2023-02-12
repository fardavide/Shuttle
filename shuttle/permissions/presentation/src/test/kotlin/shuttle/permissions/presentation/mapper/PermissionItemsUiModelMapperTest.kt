package shuttle.permissions.presentation.mapper

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import io.mockk.every
import io.mockk.mockk
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import shuttle.permissions.domain.model.BackgroundLocation
import shuttle.permissions.domain.model.CoarseLocation
import shuttle.permissions.domain.model.FineLocation
import shuttle.permissions.domain.usecase.HasBackgroundLocation
import shuttle.permissions.domain.usecase.HasCoarseLocation
import shuttle.permissions.domain.usecase.HasFineLocation
import shuttle.permissions.presentation.model.PermissionItem
import shuttle.permissions.presentation.model.PermissionItemsUiModel
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
@OptIn(ExperimentalPermissionsApi::class)
internal class PermissionItemsUiModelMapperTest(
    parameters: Parameters
) {

    private val input = parameters.input
    private val expected = parameters.expected

    private val hasBackgroundLocation: HasBackgroundLocation = mockk {
        every { this@mockk(any()) } returns input.hasBackgroundLocation
    }
    private val hasCoarseLocation: HasCoarseLocation = mockk {
        every { this@mockk(any()) } returns input.hasCoarseLocation
    }
    private val hasFineLocation: HasFineLocation = mockk {
        every { this@mockk(any()) } returns input.hasFineLocation
    }
    private val mapper = PermissionItemsUiModelMapper(
        hasBackgroundLocation = hasBackgroundLocation,
        hasCoarseLocation = hasCoarseLocation,
        hasFineLocation = hasFineLocation
    )

    @Test
    fun test() {
        val result = mapper.toUiModel(input.permissionsState, input.isAccessibilityServiceEnabled)
        assertEquals(expected, result)
    }

    data class Input(
        val hasCoarseLocation: Boolean,
        val hasFineLocation: Boolean,
        val hasBackgroundLocation: Boolean,
        val isAccessibilityServiceEnabled: Boolean
    ) {

        val permissionsState: MultiplePermissionsState = buildMultiplePermissionsState(
            hasBackgroundLocation = hasBackgroundLocation,
            hasCoarseLocation = hasCoarseLocation,
            hasFineLocation = hasFineLocation
        )
    }

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
                    hasCoarseLocation = true,
                    hasFineLocation = true,
                    hasBackgroundLocation = true,
                    isAccessibilityServiceEnabled = true
                ),
                expected = PermissionItemsUiModel(
                    PermissionItem.Location.Coarse.Granted,
                    PermissionItem.Location.Fine.Granted,
                    PermissionItem.Location.Background.Granted,
                    PermissionItem.Accessibility.Granted
                )
            ),

            Parameters(
                testName = "None granted",
                input = Input(
                    hasCoarseLocation = false,
                    hasFineLocation = false,
                    hasBackgroundLocation = false,
                    isAccessibilityServiceEnabled = false
                ),
                expected = PermissionItemsUiModel(
                    PermissionItem.Location.Coarse.NotGranted,
                    PermissionItem.Location.Fine.NotGranted,
                    PermissionItem.Location.Background.NotGranted,
                    PermissionItem.Accessibility.NotGranted
                )
            ),

            Parameters(
                testName = "Only all location granted",
                input = Input(
                    hasCoarseLocation = true,
                    hasFineLocation = true,
                    hasBackgroundLocation = true,
                    isAccessibilityServiceEnabled = false
                ),
                expected = PermissionItemsUiModel(
                    PermissionItem.Location.Coarse.Granted,
                    PermissionItem.Location.Fine.Granted,
                    PermissionItem.Location.Background.Granted,
                    PermissionItem.Accessibility.NotGranted
                )
            ),

            Parameters(
                testName = "Only accessibility granted",
                input = Input(
                    hasCoarseLocation = false,
                    hasFineLocation = false,
                    hasBackgroundLocation = false,
                    isAccessibilityServiceEnabled = true
                ),
                expected = PermissionItemsUiModel(
                    PermissionItem.Location.Coarse.NotGranted,
                    PermissionItem.Location.Fine.NotGranted,
                    PermissionItem.Location.Background.NotGranted,
                    PermissionItem.Accessibility.Granted
                )
            ),

            Parameters(
                testName = "Only coarse and fine location granted",
                input = Input(
                    hasCoarseLocation = true,
                    hasFineLocation = true,
                    hasBackgroundLocation = false,
                    isAccessibilityServiceEnabled = false
                ),
                expected = PermissionItemsUiModel(
                    PermissionItem.Location.Coarse.Granted,
                    PermissionItem.Location.Fine.Granted,
                    PermissionItem.Location.Background.NotGranted,
                    PermissionItem.Accessibility.NotGranted
                )
            )

        ).map { arrayOf(it) }

        private fun buildMultiplePermissionsState(
            hasCoarseLocation: Boolean,
            hasFineLocation: Boolean,
            hasBackgroundLocation: Boolean
        ): MultiplePermissionsState = mockk {
            every { permissions } returns listOf(
                buildPermissionState(CoarseLocation, hasCoarseLocation),
                buildPermissionState(FineLocation, hasFineLocation),
                buildPermissionState(BackgroundLocation, hasBackgroundLocation)
            )
        }

        private fun buildPermissionState(permission: String, isGranted: Boolean): PermissionState = mockk {
            every { this@mockk.permission } returns permission
            every { this@mockk.status } returns
                if (isGranted) PermissionStatus.Granted else PermissionStatus.Denied(shouldShowRationale = true)
        }
    }
}
