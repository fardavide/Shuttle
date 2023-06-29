package shuttle.settings.presentation.mapper

import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory
import shuttle.resources.R.plurals
import shuttle.resources.R.string
import shuttle.resources.TextRes
import shuttle.settings.domain.model.KeepStatisticsFor
import shuttle.settings.presentation.model.KeepStatisticsForUiModel
import shuttle.settings.presentation.model.StatisticsSettingsUiModel
import shuttle.settings.presentation.ui.component.SliderItem
import shuttle.utils.kotlin.inWholeMonths

@Factory
internal class StatisticsSettingsUiModelMapper {

    private val allValues: Map<KeepStatisticsFor, SliderItem.NamedValue> by lazy {
        KeepStatisticsFor.values().withIndex().associate { (index, keepStatisticsFor) ->
            keepStatisticsFor to when (keepStatisticsFor) {
                KeepStatisticsFor.Forever -> SliderItem.NamedValue(
                    name = TextRes(string.settings_statistics_keep_forever),
                    value = index
                )

                else -> SliderItem.NamedValue(
                    nameRes = plurals.settings_statistics_months,
                    quantity = keepStatisticsFor.duration.inWholeMonths,
                    value = index
                )
            }
        }
    }

    fun toUiModel(keepStatisticsFor: KeepStatisticsFor) = StatisticsSettingsUiModel(
        allSlideItemValues = allValues.values.toImmutableList(),
        currentValueUiModel = KeepStatisticsForUiModel(
            keepStatisticsFor = keepStatisticsFor,
            sliderItemValue = allValues.getValue(keepStatisticsFor)
        )
    )

    fun toKeepStatisticsFor(sliderItemValue: SliderItem.NamedValue): KeepStatisticsFor = allValues.entries
        .first { it.value == sliderItemValue }
        .key
}
