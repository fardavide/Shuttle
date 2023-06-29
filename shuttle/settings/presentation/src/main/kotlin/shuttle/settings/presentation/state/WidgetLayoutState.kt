package shuttle.settings.presentation.state

import shuttle.design.model.WidgetLayoutUiModel
import shuttle.design.model.WidgetPreviewAppUiModel
import shuttle.settings.domain.model.WidgetSettings

sealed interface WidgetLayoutState {

    object Loading : WidgetLayoutState
    data class Data(
        val previewApps: List<WidgetPreviewAppUiModel>,
        val widgetSettingsDomainModel: WidgetSettings,
        val layout: WidgetLayoutUiModel
    ) : WidgetLayoutState

    data class Error(val message: String) : WidgetLayoutState
}
