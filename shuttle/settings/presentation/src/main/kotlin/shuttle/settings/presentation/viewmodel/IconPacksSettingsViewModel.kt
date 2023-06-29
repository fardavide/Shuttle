package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.icons.domain.usecase.ObserveInstalledIconPacks
import shuttle.settings.domain.usecase.ObserveCurrentIconPack
import shuttle.settings.domain.usecase.SetCurrentIconPack
import shuttle.settings.presentation.action.IconPacksSettingsAction
import shuttle.settings.presentation.mapper.IconPackSettingsUiModelMapper
import shuttle.settings.presentation.model.IconPackSettingsItemUiModel
import shuttle.settings.presentation.state.IconPacksSettingsState
import shuttle.util.android.viewmodel.ShuttleViewModel

@KoinViewModel
internal class IconPacksSettingsViewModel(
    private val iconPackSettingsMapper: IconPackSettingsUiModelMapper,
    observeInstalledIconPacks: ObserveInstalledIconPacks,
    observeCurrentIconPack: ObserveCurrentIconPack,
    private val setCurrentIconPack: SetCurrentIconPack
) : ShuttleViewModel<IconPacksSettingsAction, IconPacksSettingsState>(initialState = IconPacksSettingsState.Loading) {

    init {
        combine(
            observeInstalledIconPacks(),
            observeCurrentIconPack().onStart { emit(None) }
        ) { iconPacks, currentIconPack ->
            val uiModels = iconPackSettingsMapper.toUiModels(iconPacks, currentIconPack)
            IconPacksSettingsState.Data(uiModels.filterRight())
        }
            .onEach(::emit)
            .launchIn(viewModelScope)
    }

    override fun submit(action: IconPacksSettingsAction) {
        val currentState = state.value as? IconPacksSettingsState.Data ?: return
        viewModelScope.launch {
            val newState = when (action) {
                is IconPacksSettingsAction.SetCurrentIconPack -> setCurrentIconPack(currentState, action.iconPackId)
            }
            emit(newState)
        }
    }

    private suspend fun setCurrentIconPack(
        currentState: IconPacksSettingsState.Data,
        iconPackId: Option<AppId>
    ): IconPacksSettingsState {
        val newData = currentState.iconPackSettingItems.map { uiModel ->
            when (uiModel) {
                is IconPackSettingsItemUiModel.SystemDefault -> uiModel.copy(isSelected = iconPackId.isEmpty())
                is IconPackSettingsItemUiModel.FromApp -> uiModel.copy(isSelected = uiModel.id == iconPackId.orNull())
            }
        }.toImmutableList()
        viewModelScope.launch {
            setCurrentIconPack(iconPackId)
        }
        return IconPacksSettingsState.Data(newData)
    }

    @Suppress("MaxLineLength")
    private fun List<Either<GetSystemIconError, IconPackSettingsItemUiModel>>.filterRight(): ImmutableList<IconPackSettingsItemUiModel> =
        mapNotNull {
            it.getOrNull()
        }.toImmutableList()

}
