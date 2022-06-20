package shuttle.coordinates.domain.error

sealed interface LocationError {
    object ExpiredLocation : LocationError
    object MissingPermissions : LocationError
    object NoCachedLocation : LocationError
    object Timeout : LocationError
}
