package shuttle.predictions.presentation.model

import androidx.annotation.StringRes
import shuttle.design.R
import shuttle.predictions.domain.error.ObserveSuggestedAppsError

sealed interface SuggestedAppsState {

    data class Data(
        val apps: List<WidgetAppUiModel>,
        val widgetSettings: WidgetSettingsUiModel
    ) : SuggestedAppsState

    data class Error(@StringRes val message: Int) : SuggestedAppsState {

        companion object {

            fun from(error: ObserveSuggestedAppsError): Error {
                val message = when (error) {
                    ObserveSuggestedAppsError.LocationNotAvailable -> R.string.predictions_location_not_available
                }
                return Error(message)
            }
        }
    }
}
