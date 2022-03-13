package shuttle.permissions.resources

internal object Strings {

    object Accessibility {

        const val Name = "Accessibility service"
        const val Description = "Shuttle needs access to the Accessibility Service, in order to improve the " +
            "suggestions.\nBy enabling it you consent Shuttle to detect app launching and store it to the " +
            "local database"
        const val Action = "Enable accessibility service"
        const val PermissionGrantedDescription = "Accessibility service is enabled"
        const val PermissionNotGrantedDescription = "Accessibility service is not enabled"

        object Dialog {

            const val Disclosure = "$Description\n\nIn the accessibility management screen (or in \"Installed " +
                "services\" in some devices) find 'Shuttle' and enable it. Shortcut is not necessary"
            const val ConfirmAction = "Confirm"
            const val CancelAction = "Cancel"
        }
    }

    object Location {

        const val Action = "Allow location permissions"

        object Coarse {

            const val Name = "Location permissions"
            const val Description = "Shuttle needs access to Location, in order to show suggested apps, based on " +
                "your current location\nBy allowing the permission you consent Shuttle to store a Geo Hash of your " +
                "current position and store it to the local database"
            const val PermissionGrantedDescription = "Approximate location permission is granted"
            const val PermissionNotGrantedDescription = "Approximate location permission is not granted"
        }

        object Fine {

            const val Name = "Precise location permissions"
            const val Description = "Shuttle needs access to Precise Location, in order to create a Geo Hash to " +
                "store to the local database"
            const val PermissionGrantedDescription = "Precise location permission is granted"
            const val PermissionNotGrantedDescription = "Precise location permission is not granted"
        }

        object Background {

            const val Name = "Background location permissions"
            const val Description = "Shuttle needs access to Background Location, in order to create a Geo Hash to " +
                "store to the local database every time an app has been opened. Additionally, Shuttle will use the " +
                "current location in the Widget, in order to provide suggestions based on the current location"
            const val PermissionGrantedDescription = "Background location permission is granted"
            const val PermissionNotGrantedDescription = "Background location permission is not granted"
        }
    }
}
