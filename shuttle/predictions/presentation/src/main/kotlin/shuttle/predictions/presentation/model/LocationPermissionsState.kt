package shuttle.predictions.presentation.model

internal sealed interface LocationPermissionsState {

    /**
     * Both Coarse and Fine Location Permissions are granted
     */
    object AllGranted : LocationPermissionsState

    sealed interface Pending : LocationPermissionsState {

        /**
         * First time the user sees this feature or the user doesn't want to be asked again
         */
        object Init : LocationPermissionsState.Pending

        /**
         * Only Coarse Location Permission is granted, but Fine Location Permission
         */
        object CoarseOnly : LocationPermissionsState.Pending

        /**
         * Both Location Permissions have been denied by the user
         */
        object AllDenied : LocationPermissionsState.Pending
    }
}
