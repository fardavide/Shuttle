package shuttle.predictions.domain.error

sealed interface ObserveSuggestedAppsError {

    object LocationNotAvailable : ObserveSuggestedAppsError
}
