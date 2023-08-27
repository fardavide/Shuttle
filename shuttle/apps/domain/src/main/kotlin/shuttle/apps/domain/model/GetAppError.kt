package shuttle.apps.domain.model

sealed interface GetAppError {

    data object AppNotInstalled : GetAppError

    /**
     * android.os.DeadSystemRuntimeException: android.os.DeadSystemException
     */
    data object System : GetAppError
}
