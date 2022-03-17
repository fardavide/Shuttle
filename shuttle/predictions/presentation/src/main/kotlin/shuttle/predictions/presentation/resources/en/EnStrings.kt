package shuttle.predictions.presentation.resources.en

import shuttle.predictions.presentation.resources.Strings

internal object EnStrings : Strings {

    override val AppIconContentDescription = "Application icon"
    override val SettingsIconContentDescription = "Settings icon"
    override val ShuttleTitle = "Shuttle"

    object Error : Strings.Error {

        override val LocationNotAvailable = "Location is currently not available"
    }
}
