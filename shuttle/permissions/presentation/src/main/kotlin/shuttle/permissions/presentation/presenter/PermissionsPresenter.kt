package shuttle.permissions.presentation.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentComposer
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import shuttle.accessibility.usecase.IsLaunchCounterServiceEnabled
import shuttle.permissions.domain.model.backgroundPermissionsList
import shuttle.permissions.domain.model.foregroundPermissionsList
import shuttle.permissions.presentation.action.PermissionsAction
import shuttle.permissions.presentation.mapper.PermissionItemsUiModelMapper
import shuttle.permissions.presentation.state.PermissionsState
import shuttle.utils.kotlin.tickerFlow
import kotlin.time.Duration.Companion.seconds

@Factory
internal class PermissionsPresenter(
    private val isLaunchCounterServiceEnabled: IsLaunchCounterServiceEnabled,
    private val permissionItemsUiModelMapper: PermissionItemsUiModelMapper
) {

    private val isLaunchCounterServiceEnabledFlow = tickerFlow(5.seconds) {
        isLaunchCounterServiceEnabled()
    }

    @Composable
    @OptIn(InternalComposeApi::class)
    fun models(
        actions: Flow<PermissionsAction>,
        providedValues: Array<ProvidedValue<out Any>>
    ): PermissionsState {
        val isLaunchCounterServiceEnabled = isLaunchCounterServiceEnabledFlow.collectAsState(null).value
            ?: return PermissionsState.Loading

        currentComposer.startProviders(providedValues)
        val foregroundLocationPermissionsState = rememberMultiplePermissionsState(foregroundPermissionsList)
        val backgroundLocationPermissionsState = rememberMultiplePermissionsState(backgroundPermissionsList)
        currentComposer.endProviders()

        LaunchedEffect(actions) {
            actions.collect { action ->
                when (action) {
                    PermissionsAction.RequestBackgroundLocation ->
                        backgroundLocationPermissionsState.launchMultiplePermissionRequest()
                    PermissionsAction.RequestLocation ->
                        foregroundLocationPermissionsState.launchMultiplePermissionRequest()
                }
            }
        }

        val uiModel = permissionItemsUiModelMapper.toUiModel(
            backgroundLocationPermissionsState,
            isLaunchCounterServiceEnabled
        )
        return if (uiModel.areAllGranted()) PermissionsState.AllGranted
        else PermissionsState.Pending(uiModel)
    }
}
