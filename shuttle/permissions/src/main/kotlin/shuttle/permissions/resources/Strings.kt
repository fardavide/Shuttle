package shuttle.permissions.resources

internal object Strings {

    object Accessibility {

        const val Title = "Enable accessibility service"
        const val Description = "The accessibility service will need to retrieve only package of the open " +
            "application, in order to improve the suggestions"
        const val Action = "Enable now"
    }

    object Location {

        const val Action = "Allow permissions"

        object Coarse {

            const val Title = "Allow location permissions"
            const val Description = "The location permissions is needed, in order to improve the suggestions, " +
                "related to your current location"
        }

        object Fine {

            const val Title = "Allow fine location permissions"
            const val Description = "The precise location permissions is needed, because the approximate location " +
                "has a too wide range to provide precise suggestions"
        }

        object Background {

            const val Title = "Allow background location permissions"
            const val Description = "The background location permissions is needed, in order to update the widget " +
                "and the suggestions"
        }
    }
}
