package shuttle.settings.presentation.model

import kotlinx.collections.immutable.ImmutableList
import shuttle.settings.domain.model.KeepStatisticsFor
import shuttle.settings.presentation.ui.component.SliderItem

internal data class StatisticsSettingsUiModel(
    val allSlideItemValues: ImmutableList<SliderItem.NamedValue>,
    val currentValueUiModel: KeepStatisticsForUiModel
)

internal data class KeepStatisticsForUiModel(
    val keepStatisticsFor: KeepStatisticsFor,
    val sliderItemValue: SliderItem.NamedValue
)
