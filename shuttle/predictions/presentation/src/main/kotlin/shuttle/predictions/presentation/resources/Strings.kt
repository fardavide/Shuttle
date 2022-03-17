package shuttle.predictions.presentation.resources

import shuttle.design.StringResource
import shuttle.design.SupportedLanguage
import shuttle.design.getLanguage
import shuttle.predictions.presentation.resources.en.EnStrings
import shuttle.predictions.presentation.resources.it.ItStrings

internal interface Strings {

    val AppIconContentDescription: String
    val SettingsIconContentDescription: String
    val ShuttleTitle: String

    interface Error {

        val LocationNotAvailable: String
    }
}

@JvmName("getFromStrings")
internal fun StringResource<Strings>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings
        SupportedLanguage.Italian -> ItStrings
    }
    return get(receiver)
}

@JvmName("getFromStringsError")
internal fun StringResource<Strings.Error>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings.Error
        SupportedLanguage.Italian -> ItStrings.Error
    }
    return get(receiver)
}
