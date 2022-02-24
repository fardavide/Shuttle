package shuttle.predictions.presentation.resources

internal object Strings {

    object Action {

        const val AllowPreciseLocation = "Allow precise location"
        const val RequestPermissions = "Request permissions"
    }

    object Message {

        const val RequestLocation = "Shuttle will suggest you the best apps for you, memorizing the location where " +
            "you use them most. Please grant precise location permission, in order to have the best results."
        const val RequestPreciseLocation = "Ops, seems like you granted only granted the approximate location " +
            "permission, this is not enough for the app to work at its best. Please grant precise location permission."
        const val LocationFeatureDisabled = "Seems like you didn't grant the location permissions :( so your apps " +
            "will be sorted without considering the usage related to location."
    }
}
