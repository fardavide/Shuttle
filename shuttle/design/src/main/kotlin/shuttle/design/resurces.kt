package shuttle.design

import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import kotlin.reflect.KProperty1

typealias StringResource <T> = KProperty1<T, String>
typealias AnyStringResource = StringResource<*>

fun getLanguage(): SupportedLanguage {
    val language = ConfigurationCompat.getLocales(Resources.getSystem().configuration)
        .get(0)
        .language

    return when (language) {
        "en" -> SupportedLanguage.English
        "it" -> SupportedLanguage.Italian
        else -> SupportedLanguage.English
    }
}

enum class SupportedLanguage {

    English,
    Italian
}
