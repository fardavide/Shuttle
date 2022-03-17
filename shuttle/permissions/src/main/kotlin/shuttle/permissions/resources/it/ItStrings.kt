@file:Suppress("SpellCheckingInspection")

package shuttle.permissions.resources.it

import shuttle.permissions.resources.Strings

internal object ItStrings : Strings {

    object Accessibility : Strings.Accessibility {

        override val Name = "Servizio di Accessibilità"
        override val Description = "Shuttle necessita the Servizio di Accessibilità, per migliorare i suggerimenti.\n" +
            "Abilitando il servizio accetti che Shuttle rilevi gli avvii delle app e li salvi sul database locale"
        override val Action = "Abilita il servizio di accessiblità"
        override val PermissionGrantedDescription = "Servizio di accessibilità abilitato"
        override val PermissionNotGrantedDescription = "Servizio di accessibilità non abilitato"

        object Dialog : Strings.Accessibility.Dialog {

            override val Disclosure = "$Description\n\nNella schermata di gestione dei servizi di accessibilità ( " +
                "oppure \"Servizi installati\" per alcuni dispositivi ) cerca 'Shuttle' ed abilitalo. La scorciatoia " +
                "non è necessaria"
            override val ConfirmAction = "Conferma"
            override val CancelAction = "Annulla"
        }
    }

    object Location : Strings.Location {

        override val Action = "Abilita i permessi di locazione"

        object Coarse : Strings.Location.Coarse {

            override val Name = "Permessi di locazione"
            override val Description = "Shuttle necessita di accedere alla posizione, in modo da mostrare le " +
                "applicazioni suggerite, in base alla tua posizione corrente\nAbilitando i permessi acconsenti che " +
                "Shuttle salvi un Geo Hash della tua posizione nel database locale"
            override val PermissionGrantedDescription = "Permessi di locazione approssivativa abilitati"
            override val PermissionNotGrantedDescription = "Permessi di locazione approssivativa non abilitati"
        }

        object Fine : Strings.Location.Fine {

            override val Name = "Permessi di locazione precisa"
            override val Description = "Shuttle necessita l'accesso alla posizione precita, in modo da create un Geo " +
                "Hash da salvere nel database locale"
            override val PermissionGrantedDescription = "Permessi di locazione precisa abilitati"
            override val PermissionNotGrantedDescription = "Permessi di locazione precisa non abilitati"
        }

        object Background : Strings.Location.Background {

            override val Name = "Permessi di locazione in background"
            override val Description = "Shuttle necessita l'accesso alla posizione in background, in modo da creare " +
                "un Geo Hash, da salvere nel database locale, ogni volta che un app viene aperta. Inoltre, Shuttle " +
                "userà la posizione corrente nel Widget, in modo da mostrare suggerimenti in base all posizione " +
                "corrente"
            override val PermissionGrantedDescription = "Permessi di locazione in background abilitati"
            override val PermissionNotGrantedDescription = "Permessi di locazione in background non abilitati"
        }
    }
}
