package shuttle.coordinates.domain.error

sealed interface LocationError {
    object NoCachedLocation: LocationError
    object ExpiredLocation: LocationError
}
