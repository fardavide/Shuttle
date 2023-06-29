package shuttle.settings.presentation.action

import shuttle.settings.presentation.ui.component.SliderItem

internal sealed interface StatisticsSettingsAction {

    @JvmInline
    value class SetKeepStatisticsFor(val sliderItemValue: SliderItem.NamedValue) : StatisticsSettingsAction
}
