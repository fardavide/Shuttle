package shuttle.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import shuttle.settings.domain.usecase.ObserveKeepStatisticsFor
import shuttle.settings.domain.usecase.SetKeepStatisticsFor
import shuttle.settings.presentation.action.StatisticsSettingsAction
import shuttle.settings.presentation.mapper.StatisticsSettingsUiModelMapper
import shuttle.settings.presentation.state.StatisticsSettingsState
import shuttle.util.android.viewmodel.ShuttleViewModel

@KoinViewModel
internal class StatisticsSettingsViewModel(
    observeKeepStatisticsFor: ObserveKeepStatisticsFor,
    private val setKeepStatisticsFor: SetKeepStatisticsFor,
    private val statisticsSettingsUiModelMapper: StatisticsSettingsUiModelMapper
) : ShuttleViewModel<StatisticsSettingsAction, StatisticsSettingsState>(StatisticsSettingsState.Loading) {

    init {
        observeKeepStatisticsFor()
            .map { StatisticsSettingsState.Data(statisticsSettingsUiModelMapper.toUiModel(it)) }
            .onEach(::emit)
            .launchIn(viewModelScope)
    }

    override fun submit(action: StatisticsSettingsAction) {
        viewModelScope.launch {
            when (action) {
                is StatisticsSettingsAction.SetKeepStatisticsFor ->
                    setKeepStatisticsFor(statisticsSettingsUiModelMapper.toKeepStatisticsFor(action.sliderItemValue))
            }
        }
    }
}
