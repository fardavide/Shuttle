@file:Suppress("SpellCheckingInspection")

package shuttle.settings.presentation.resources.it

import shuttle.settings.presentation.resources.Strings

internal object ItStrings : Strings {

    override val AppIconContentDescription = "Icona app"
    override val SettingsTitle = "Impostazioni"

    object Blacklist : Strings.Blacklist {

        override val Title = "Blacklist"
        override val Description = "Le app selezionate non verranno proposte come suggerimenti"
    }

    object WidgetSettings : Strings.WidgetSettings {

        override val Title = "Impostazioni Widget"
        override val Description = "Customizza l'aspetto del Widget"

        override val RowsCount = "Numero righe"
        override val ColumnsCount = "Numero colonne"
        override val IconsSize = "Dimensioni icone"
        override val VerticalSpacing = "Spazio verticale tra le icone"
        override val HorizontalSpacing = "Spazio orizzontale tra le icone"
        override val TextSize = "Dimensione test"

        override val DpUnit = "Dp"
        override val SpUnit = "Sp"
    }
}
