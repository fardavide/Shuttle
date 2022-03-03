@file:OptIn(ExperimentalPermissionsApi::class)

package shuttle.permissions.mapper

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Build
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import io.mockk.every
import io.mockk.mockk
import shuttle.permissions.model.LocationPermissionsState
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.test.Test
import kotlin.test.assertEquals

class LocationPermissionsStateMapperTest {

    private val mapper = LocationPermissionsStateMapper()

    @Test
    fun `all permissions granted before Android Q`() {
        // given
        beforeAndroidQ()
        val state = buildMultiplePermissionsState(
            listOf(
                buildPermissionState(ACCESS_COARSE_LOCATION, PermissionStatus.Granted),
                buildPermissionState(ACCESS_FINE_LOCATION, PermissionStatus.Granted)
            )
        )
        val expected = LocationPermissionsState.AllGranted

        // when
        val result = mapper.toLocationPermissionState(state)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `all permissions granted after Android Q`() {
        // given
        afterAndroidQ()
        val state = buildMultiplePermissionsState(
            listOf(
                buildPermissionState(ACCESS_COARSE_LOCATION, PermissionStatus.Granted),
                buildPermissionState(ACCESS_FINE_LOCATION, PermissionStatus.Granted),
                buildPermissionState(ACCESS_BACKGROUND_LOCATION, PermissionStatus.Granted)
            )
        )
        val expected = LocationPermissionsState.AllGranted

        // when
        val result = mapper.toLocationPermissionState(state)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `no background location after Android Q with rationale`() {
        // given
        afterAndroidQ()
        val state = buildMultiplePermissionsState(
            listOf(
                buildPermissionState(ACCESS_COARSE_LOCATION, PermissionStatus.Granted),
                buildPermissionState(ACCESS_FINE_LOCATION, PermissionStatus.Granted),
                buildPermissionState(ACCESS_BACKGROUND_LOCATION, PermissionStatus.Denied(shouldShowRationale = true))
            )
        )
        val expected = LocationPermissionsState.Pending.MissingBackground

        // when
        val result = mapper.toLocationPermissionState(state)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `no background location after Android Q without rationale`() {
        // given
        afterAndroidQ()
        val state = buildMultiplePermissionsState(
            listOf(
                buildPermissionState(ACCESS_COARSE_LOCATION, PermissionStatus.Granted),
                buildPermissionState(ACCESS_FINE_LOCATION, PermissionStatus.Granted),
                buildPermissionState(ACCESS_BACKGROUND_LOCATION, PermissionStatus.Denied(shouldShowRationale = false))
            )
        )
        val expected = LocationPermissionsState.Pending.MissingBackground

        // when
        val result = mapper.toLocationPermissionState(state)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `coarse only before Android Q with rational`() {
        // given
        beforeAndroidQ()
        val state = buildMultiplePermissionsState(
            permissions = listOf(
                buildPermissionState(ACCESS_COARSE_LOCATION, PermissionStatus.Granted),
                buildPermissionState(ACCESS_FINE_LOCATION, PermissionStatus.Denied(shouldShowRationale = true))
            )
        )
        val expected = LocationPermissionsState.Pending.CoarseOnly

        // when
        val result = mapper.toLocationPermissionState(state)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `coarse only before Android Q without rational`() {
        // given
        beforeAndroidQ()
        val state = buildMultiplePermissionsState(
            permissions = listOf(
                buildPermissionState(ACCESS_COARSE_LOCATION, PermissionStatus.Granted),
                buildPermissionState(ACCESS_FINE_LOCATION, PermissionStatus.Denied(shouldShowRationale = false))
            )
        )
        val expected = LocationPermissionsState.Pending.CoarseOnly

        // when
        val result = mapper.toLocationPermissionState(state)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `coarse only after Android Q with rational`() {
        // given
        afterAndroidQ()
        val state = buildMultiplePermissionsState(
            permissions = listOf(
                buildPermissionState(ACCESS_COARSE_LOCATION, PermissionStatus.Granted),
                buildPermissionState(ACCESS_FINE_LOCATION, PermissionStatus.Denied(shouldShowRationale = true)),
                buildPermissionState(ACCESS_BACKGROUND_LOCATION, PermissionStatus.Denied(shouldShowRationale = false))
            )
        )
        val expected = LocationPermissionsState.Pending.CoarseOnly

        // when
        val result = mapper.toLocationPermissionState(state)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `coarse only after Android Q without rational`() {
        // given
        afterAndroidQ()
        val state = buildMultiplePermissionsState(
            permissions = listOf(
                buildPermissionState(ACCESS_COARSE_LOCATION, PermissionStatus.Granted),
                buildPermissionState(ACCESS_FINE_LOCATION, PermissionStatus.Denied(shouldShowRationale = false)),
                buildPermissionState(ACCESS_BACKGROUND_LOCATION, PermissionStatus.Denied(shouldShowRationale = false))
            )
        )
        val expected = LocationPermissionsState.Pending.CoarseOnly

        // when
        val result = mapper.toLocationPermissionState(state)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `all denied before Android Q with rationale`() {
        // given
        beforeAndroidQ()
        val state = buildMultiplePermissionsState(
            permissions = listOf(
                buildPermissionState(ACCESS_COARSE_LOCATION, PermissionStatus.Denied(shouldShowRationale = true)),
                buildPermissionState(ACCESS_FINE_LOCATION, PermissionStatus.Denied(shouldShowRationale = true))
            )
        )
        val expected = LocationPermissionsState.Pending.Init

        // when
        val result = mapper.toLocationPermissionState(state)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `all denied before Android Q without rationale`() {
        // given
        beforeAndroidQ()
        val state = buildMultiplePermissionsState(
            permissions = listOf(
                buildPermissionState(ACCESS_COARSE_LOCATION, PermissionStatus.Denied(shouldShowRationale = false)),
                buildPermissionState(ACCESS_FINE_LOCATION, PermissionStatus.Denied(shouldShowRationale = true))
            )
        )
        val expected = LocationPermissionsState.Pending.Init

        // when
        val result = mapper.toLocationPermissionState(state)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `all denied after Android Q with rationale`() {
        // given
        afterAndroidQ()
        val state = buildMultiplePermissionsState(
            permissions = listOf(
                buildPermissionState(ACCESS_COARSE_LOCATION, PermissionStatus.Denied(shouldShowRationale = true)),
                buildPermissionState(ACCESS_FINE_LOCATION, PermissionStatus.Denied(shouldShowRationale = false)),
                buildPermissionState(ACCESS_BACKGROUND_LOCATION, PermissionStatus.Denied(shouldShowRationale = false))
            )
        )
        val expected = LocationPermissionsState.Pending.Init

        // when
        val result = mapper.toLocationPermissionState(state)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `all denied after Android Q without rationale`() {
        // given
        afterAndroidQ()
        val state = buildMultiplePermissionsState(
            permissions = listOf(
                buildPermissionState(ACCESS_COARSE_LOCATION, PermissionStatus.Denied(shouldShowRationale = false)),
                buildPermissionState(ACCESS_FINE_LOCATION, PermissionStatus.Denied(shouldShowRationale = false)),
                buildPermissionState(ACCESS_BACKGROUND_LOCATION, PermissionStatus.Denied(shouldShowRationale = false))
            )
        )
        val expected = LocationPermissionsState.Pending.Init

        // when
        val result = mapper.toLocationPermissionState(state)

        // then
        assertEquals(expected, result)
    }

    private fun beforeAndroidQ() {
        setAndroidVersion(Build.VERSION_CODES.P)
    }

    private fun afterAndroidQ() {
        setAndroidVersion(Build.VERSION_CODES.R)
    }

    private fun setAndroidVersion(androidVersion: Int) {
        val field = Build.VERSION::class.java.getField("SDK_INT").apply {
            isAccessible = true
        }
        Field::class.java.getDeclaredField("modifiers").apply {
            isAccessible = true
            setInt(field, field.modifiers and Modifier.FINAL.inv())
        }
        field.set(null, androidVersion)
    }

    companion object TestData {

        private fun buildMultiplePermissionsState(
            permissions: List<PermissionState> = emptyList()
        ): MultiplePermissionsState = mockk(relaxUnitFun = true) {
            every { this@mockk.permissions } returns permissions
            every { this@mockk.allPermissionsGranted } answers {
                permissions.all { it.status.isGranted } || revokedPermissions.isEmpty()
            }
            every { this@mockk.revokedPermissions } answers {
                permissions.filter { it.status != PermissionStatus.Granted }
            }
            every { this@mockk.shouldShowRationale } answers {
                permissions.any { it.status.shouldShowRationale }
            }
        }

        private fun buildPermissionState(
            permission: String,
            status: PermissionStatus
        ): PermissionState = mockk(relaxUnitFun = true) {
            every { this@mockk.permission } returns permission
            every { this@mockk.status } returns status
        }
    }
}

