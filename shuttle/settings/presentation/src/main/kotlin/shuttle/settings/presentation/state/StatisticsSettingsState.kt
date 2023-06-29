package shuttle.settings.presentation.state

import shuttle.settings.presentation.model.StatisticsSettingsUiModel

internal sealed interface StatisticsSettingsState {
    object Loading : StatisticsSettingsState

    @JvmInline
    value class Data(

        val uiModel: StatisticsSettingsUiModel
    ) : StatisticsSettingsState
}
