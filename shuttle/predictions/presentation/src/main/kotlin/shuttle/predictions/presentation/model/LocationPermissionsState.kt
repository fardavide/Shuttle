package shuttle.predictions.presentation.model

internal sealed interface LocationPermissionsState {

    /**
     * All Location Permissions are granted: Coarse, Fine and Background, for Android API 29
     */
    object AllGranted : LocationPermissionsState

    sealed interface Pending : LocationPermissionsState {

        /**
         * First time the user sees this feature or the user doesn't want to be asked again
         */
        object Init : Pending

        /**
         * Only Coarse Location Permission is granted, but Fine and Background Location Permission
         */
        object CoarseOnly : Pending

        /**
         * Background Location Permission is not granted
         */
        object MissingBackground : Pending

        /**
         * Both Location Permissions have been denied by the user
         */
        object AllDenied : Pending
    }
}
