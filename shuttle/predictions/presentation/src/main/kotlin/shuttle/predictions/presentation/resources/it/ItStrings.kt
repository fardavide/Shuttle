@file:Suppress("SpellCheckingInspection")

package shuttle.predictions.presentation.resources.it

import shuttle.predictions.presentation.resources.Strings

internal object ItStrings : Strings {

    override val AppIconContentDescription = "Icona app"
    override val SettingsIconContentDescription = "Icona impostazioni"
    override val ShuttleTitle = "Shuttle"

    object Error : Strings.Error {

        override val LocationNotAvailable = "La locazione non è momentaneamente disponibile"
    }
}
