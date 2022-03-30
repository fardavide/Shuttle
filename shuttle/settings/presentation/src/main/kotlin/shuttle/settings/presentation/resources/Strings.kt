package shuttle.settings.presentation.resources

import shuttle.design.StringResource
import shuttle.design.SupportedLanguage.English
import shuttle.design.SupportedLanguage.Italian
import shuttle.design.getLanguage
import shuttle.settings.presentation.resources.en.EnStrings
import shuttle.settings.presentation.resources.it.ItStrings

internal interface Strings {

    val AppIconContentDescription : String
    val SettingsTitle: String

    interface Blacklist {

        val Title: String
        val Description: String
    }

    interface CheckPermissions {

        val Title: String
        val Description: String
    }

    interface IconPack {

        val Title: String
        val Description: String
        val SystemDefault: String
    }

    interface WidgetSettings {

        val Title: String
        val Description: String

        val RowsCount: String
        val ColumnsCount: String
        val IconsSize: String
        val VerticalSpacing: String
        val HorizontalSpacing: String
        val TextSize: String

        val DpUnit: String
        val SpUnit: String
    }
}

@JvmName("getFromStrings")
internal fun StringResource<Strings>.get(): String {
    val receiver =  when (getLanguage()) {
        English -> EnStrings
        Italian -> ItStrings
    }
    return get(receiver)
}

@JvmName("getFromStringsBlacklist")
internal fun StringResource<Strings.Blacklist>.get(): String {
    val receiver =  when (getLanguage()) {
        English -> EnStrings.Blacklist
        Italian -> ItStrings.Blacklist
    }
    return get(receiver)
}

@JvmName("getFromStringsCheckPermissions")
internal fun StringResource<Strings.CheckPermissions>.get(): String {
    val receiver =  when (getLanguage()) {
        English -> EnStrings.CheckPermissions
        Italian -> ItStrings.CheckPermissions
    }
    return get(receiver)
}

@JvmName("getFromStringsIconPack")
internal fun StringResource<Strings.IconPack>.get(): String {
    val receiver =  when (getLanguage()) {
        English -> EnStrings.IconPack
        Italian -> ItStrings.IconPack
    }
    return get(receiver)
}

@JvmName("getFromStringsWidgetSettings")
internal fun StringResource<Strings.WidgetSettings>.get(): String {
    val receiver =  when (getLanguage()) {
        English -> EnStrings.WidgetSettings
        Italian -> ItStrings.WidgetSettings
    }
    return get(receiver)
}
