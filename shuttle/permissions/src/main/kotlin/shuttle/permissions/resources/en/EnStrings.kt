package shuttle.permissions.resources.en

import shuttle.permissions.resources.Strings

internal object EnStrings : Strings {

    override val PermissionsTitle = "Permissions"
    override val SkipPermissionsAction = "Skip permissions"

    object Accessibility : Strings.Accessibility {

        override val Name = "Accessibility service"
        override val Description = "Shuttle needs access to the Accessibility Service, in order to improve the " +
            "suggestions.\nBy enabling it you consent Shuttle to detect app launching and store it to the " +
            "local database"
        override val Action = "Enable accessibility service"
        override val PermissionGrantedDescription = "Accessibility service is enabled"
        override val PermissionNotGrantedDescription = "Accessibility service is not enabled"

        object Dialog : Strings.Accessibility.Dialog {

            override val Disclosure = "$Description\n\nIn the accessibility management screen (or in \"Installed " +
                "services\" in some devices) find 'Shuttle' and enable it. Shortcut is not necessary"
            override val ConfirmAction = "Confirm"
            override val CancelAction = "Cancel"
        }
    }

    object Location : Strings.Location {

        override val Action = "Allow location permissions"

        object Coarse : Strings.Location.Coarse {

            override val Name = "Location permissions"
            override val Description = "Shuttle needs access to Location, in order to show suggested apps, based on " +
                "your current location\nBy allowing the permission you consent Shuttle to store a Geo Hash of your " +
                "current position and store it to the local database"
            override val PermissionGrantedDescription = "Approximate location permission is granted"
            override val PermissionNotGrantedDescription = "Approximate location permission is not granted"
        }

        object Fine : Strings.Location.Fine {

            override val Name = "Precise location permissions"
            override val Description = "Shuttle needs access to Precise Location, in order to create a Geo Hash to " +
                "store to the local database"
            override val PermissionGrantedDescription = "Precise location permission is granted"
            override val PermissionNotGrantedDescription = "Precise location permission is not granted"
        }

        object Background : Strings.Location.Background {

            override val Name = "Background location permissions"
            override val Description = "Shuttle needs access to Background Location, in order to create a Geo Hash " +
                "to store to the local database every time an app has been opened. Additionally, Shuttle will use " +
                "the current location in the Widget, in order to provide suggestions based on the current location"
            override val PermissionGrantedDescription = "Background location permission is granted"
            override val PermissionNotGrantedDescription = "Background location permission is not granted"
        }
    }
}
