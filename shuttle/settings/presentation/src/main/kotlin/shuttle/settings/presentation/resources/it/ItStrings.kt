@file:Suppress("SpellCheckingInspection")

package shuttle.settings.presentation.resources.it

import shuttle.settings.presentation.resources.Strings

internal object ItStrings : Strings {

    object Blacklist : Strings.Blacklist {

        override val Title = "Blacklist"
        override val Description = "Le app selezionate non verranno proposte come suggerimenti"
    }

    object CheckPermissions : Strings.CheckPermissions {

        override val Title = "Controlla i permessi"
        override val Description = "Verifica lo stato dei permessi necessari"
    }

    object IconPack : Strings.IconPack {

        override val Title = "Pacchetti Icone"
        override val Description = "Scegli un Pacchetto d'Icone"
        override val SystemDefault = "Icone di sistema"
    }

    object WidgetLayout : Strings.WidgetLayout {

        override val Title = "Disposizone Widget"
        override val Description = "Customizza la disposizione del Widget"

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
