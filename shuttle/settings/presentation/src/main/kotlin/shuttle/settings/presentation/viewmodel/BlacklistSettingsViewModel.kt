package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.Either
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.settings.domain.model.AppBlacklistSetting
import shuttle.settings.domain.usecase.AddToBlacklist
import shuttle.settings.domain.usecase.RemoveFromBlacklist
import shuttle.settings.domain.usecase.SearchAppsBlacklistSettings
import shuttle.settings.presentation.action.BlacklistSettingsAction
import shuttle.settings.presentation.mapper.AppBlacklistSettingUiModelMapper
import shuttle.settings.presentation.model.AppBlacklistSettingUiModel
import shuttle.settings.presentation.state.BlacklistSettingsState
import shuttle.util.android.viewmodel.ShuttleViewModel

@KoinViewModel
internal class BlacklistSettingsViewModel(
    private val appUiModelMapper: AppBlacklistSettingUiModelMapper,
    private val addToBlacklist: AddToBlacklist,
    private val removeFromBlacklist: RemoveFromBlacklist,
    private val searchAppsBlacklistSettings: SearchAppsBlacklistSettings
) : ShuttleViewModel<BlacklistSettingsAction, BlacklistSettingsState>(initialState = BlacklistSettingsState.Loading) {

    private var sortingOrder: List<AppId>? = null

    init {
        searchAppsBlacklistSettings()
            .map { list ->
                BlacklistSettingsState.Data(
                    apps = appUiModelMapper.toUiModels(list.sortIfFirstData()).filterRight()
                )
            }
            .onEach(::emit)
            .launchIn(viewModelScope)
    }

    override fun submit(action: BlacklistSettingsAction) {
        val currentState = state.value as? BlacklistSettingsState.Data ?: return
        viewModelScope.launch {
            val newState = when (action) {
                is BlacklistSettingsAction.AddToBlacklist -> onAddToBlacklist(currentState, action.appId)
                is BlacklistSettingsAction.RemoveFromBlacklist -> onRemoveFromBlacklist(currentState, action.appId)
                is BlacklistSettingsAction.Search -> onSearch(action.query)
            }
            emit(newState)
        }
    }

    private suspend fun onAddToBlacklist(
        currentState: BlacklistSettingsState.Data,
        appId: AppId
    ): BlacklistSettingsState {
        val newData = currentState.apps.map { app ->
            val shouldBeBlacklisted =
                if (app.id == appId) true
                else app.isBlacklisted
            app.copy(isBlacklisted = shouldBeBlacklisted)
        }.toImmutableList()
        viewModelScope.launch {
            addToBlacklist(appId)
        }
        return BlacklistSettingsState.Data(newData)
    }

    private suspend fun onRemoveFromBlacklist(
        currentState: BlacklistSettingsState.Data,
        appId: AppId
    ): BlacklistSettingsState {
        val newData = currentState.apps.map { app ->
            val shouldBeBlacklisted =
                if (app.id == appId) false
                else app.isBlacklisted
            app.copy(isBlacklisted = shouldBeBlacklisted)
        }.toImmutableList()
        viewModelScope.launch {
            removeFromBlacklist(appId)
        }
        return BlacklistSettingsState.Data(newData)
    }

    private fun onSearch(query: String): BlacklistSettingsState {
        searchAppsBlacklistSettings(query)
        return state.value
    }

    private fun List<AppBlacklistSetting>.sortIfFirstData(): List<AppBlacklistSetting> {
        return if (sortingOrder == null) {
            val sorted = sortedByDescending { it.inBlacklist }
            sortingOrder = sorted.map { it.app.id }
            sorted
        } else {
            sortingOrder!!.mapNotNull { sorted -> find { sorted == it.app.id } }
        }
    }

    @Suppress("MaxLineLength")
    private fun List<Either<GetSystemIconError, AppBlacklistSettingUiModel>>.filterRight(): ImmutableList<AppBlacklistSettingUiModel> =
        mapNotNull {
            it.getOrNull()
        }.toImmutableList()

}
