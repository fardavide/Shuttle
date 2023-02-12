package shuttle.utils.kotlin

infix fun String.inNoCase(other: String) = this.lowercase() in other.lowercase()
