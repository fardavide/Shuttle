package shuttle.permissions.resources

import shuttle.design.StringResource
import shuttle.design.SupportedLanguage
import shuttle.design.getLanguage
import shuttle.permissions.resources.en.EnStrings
import shuttle.permissions.resources.it.ItStrings

internal interface Strings {

    val SkipPermissionsAction: String

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

@JvmName("getFromGeneric")
internal fun StringResource<*>.get(): String {
    fun catch(block: () -> String): String? = try {
        block()
    } catch (ignored: ClassCastException) {
        null
    }
    @Suppress("UNCHECKED_CAST")
    return catch { (this as StringResource<Strings>).get() }
        ?: catch { (this as StringResource<Strings.Accessibility>).get() }
        ?: catch { (this as StringResource<Strings.Accessibility.Dialog>).get() }
        ?: catch { (this as StringResource<Strings.Location>).get() }
        ?: catch { (this as StringResource<Strings.Location.Coarse>).get() }
        ?: catch { (this as StringResource<Strings.Location.Fine>).get() }
        ?: catch { (this as StringResource<Strings.Location.Background>).get() }
        ?: throw IllegalArgumentException("Cannot resolve string for ${this::class.simpleName}")
}

@JvmName("getFromStrings")
internal fun StringResource<Strings>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings
        SupportedLanguage.Italian -> ItStrings
    }
    return get(receiver)
}

@JvmName("getFromStringsAccessibility")
internal fun StringResource<Strings.Accessibility>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings.Accessibility
        SupportedLanguage.Italian -> ItStrings.Accessibility
    }
    return get(receiver)
}

@JvmName("getFromStringsAccessibilityDialog")
internal fun StringResource<Strings.Accessibility.Dialog>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings.Accessibility.Dialog
        SupportedLanguage.Italian -> ItStrings.Accessibility.Dialog
    }
    return get(receiver)
}

@JvmName("getFromStringsLocation")
internal fun StringResource<Strings.Location>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings.Location
        SupportedLanguage.Italian -> ItStrings.Location
    }
    return get(receiver)
}

@JvmName("getFromStringsLocationCoarse")
internal fun StringResource<Strings.Location.Coarse>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings.Location.Coarse
        SupportedLanguage.Italian -> ItStrings.Location.Coarse
    }
    return get(receiver)
}

@JvmName("getFromStringsLocationFine")
internal fun StringResource<Strings.Location.Fine>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings.Location.Fine
        SupportedLanguage.Italian -> ItStrings.Location.Fine
    }
    return get(receiver)
}

@JvmName("getFromStringsLocationBackground")
internal fun StringResource<Strings.Location.Background>.get(): String {
    val receiver =  when (getLanguage()) {
        SupportedLanguage.English -> EnStrings.Location.Background
        SupportedLanguage.Italian -> ItStrings.Location.Background
    }
    return get(receiver)
}
