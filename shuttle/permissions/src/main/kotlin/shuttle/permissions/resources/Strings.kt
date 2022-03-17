package shuttle.permissions.resources

import shuttle.design.StringResource
import shuttle.design.SupportedLanguage
import shuttle.design.getLanguage
import shuttle.permissions.resources.en.EnStrings

internal interface Strings {

    interface Accessibility {

        val Name: String
        val Description: String
        val Action: String
        val PermissionGrantedDescription: String
        val PermissionNotGrantedDescription: String

        interface Dialog {

            val Disclosure: String
            val ConfirmAction: String
            val CancelAction: String
        }
    }

    interface Location {

        val Action: String

        interface Coarse {

            val Name: String
            val Description: String
            val PermissionGrantedDescription: String
            val PermissionNotGrantedDescription: String
        }

        interface Fine {

            val Name: String
            val Description: String
            val PermissionGrantedDescription: String
            val PermissionNotGrantedDescription: String
        }

        interface Background {

            val Name: String
            val Description: String
            val PermissionGrantedDescription: String
            val PermissionNotGrantedDescription: String
        }
    }
}

@JvmName("getFromStrings")
internal fun StringResource<Strings>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings
        shuttle.design.SupportedLanguage.Italian -> ItStrings
    }
    return get(receiver)
}

@JvmName("getFromStringsAccessibility")
internal fun StringResource<Strings.Accessibility>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings.Accessibility
        shuttle.design.SupportedLanguage.Italian -> ItStrings.Error
    }
    return get(receiver)
}

@JvmName("getFromStringsAccessibilityDialog")
internal fun StringResource<Strings.Accessibility.Dialog>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings.Accessibility.Dialog
        shuttle.design.SupportedLanguage.Italian -> ItStrings.Error
    }
    return get(receiver)
}

@JvmName("getFromStringsLocation")
internal fun StringResource<Strings.Location>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings.Location
        shuttle.design.SupportedLanguage.Italian -> ItStrings.Error
    }
    return get(receiver)
}

@JvmName("getFromStringsLocationCoarse")
internal fun StringResource<Strings.Location.Coarse>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings.Location.Coarse
        shuttle.design.SupportedLanguage.Italian -> ItStrings.Error
    }
    return get(receiver)
}

@JvmName("getFromStringsLocationFine")
internal fun StringResource<Strings.Location.Fine>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings.Location.Fine
        shuttle.design.SupportedLanguage.Italian -> ItStrings.Error
    }
    return get(receiver)
}

@JvmName("getFromStringsLocationBackground")
internal fun StringResource<Strings.Location.Background>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings.Location.Background
        shuttle.design.SupportedLanguage.Italian -> ItStrings.Error
    }
    return get(receiver)
}
