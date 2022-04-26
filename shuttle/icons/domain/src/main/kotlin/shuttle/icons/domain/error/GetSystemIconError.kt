package shuttle.icons.domain.error

sealed interface GetSystemIconError {

    object AppNotInstalled : GetSystemIconError
}
