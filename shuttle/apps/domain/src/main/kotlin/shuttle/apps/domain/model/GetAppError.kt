package shuttle.apps.domain.model

sealed interface GetAppError {

    data object AppNotInstalled : GetAppError
}
