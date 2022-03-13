package shuttle.permissions.model

import shuttle.permissions.resources.Strings

internal object PermissionItem {

    object Location {

        object Coarse {

            val Granted = PermissionItemUiModel.Granted(
                name = Strings.Location.Coarse.Name,
                permissionGrantedDescription = Strings.Location.Coarse.PermissionGrantedDescription
            )

            val NotGranted = PermissionItemUiModel.NotGranted(
                name = Strings.Location.Coarse.Name,
                description = Strings.Location.Coarse.Description,
                permissionNotGrantedDescription = Strings.Location.Coarse.PermissionNotGrantedDescription,
                buttonText = Strings.Location.Action
            )
        }

        object Fine {

            val Granted = PermissionItemUiModel.Granted(
                name = Strings.Location.Fine.Name,
                permissionGrantedDescription = Strings.Location.Fine.PermissionGrantedDescription
            )

            val NotGranted = PermissionItemUiModel.NotGranted(
                name = Strings.Location.Fine.Name,
                description = Strings.Location.Fine.Description,
                permissionNotGrantedDescription = Strings.Location.Fine.PermissionNotGrantedDescription,
                buttonText = Strings.Location.Action
            )
        }

        object Background {

            val Granted = PermissionItemUiModel.Granted(
                name = Strings.Location.Background.Name,
                permissionGrantedDescription = Strings.Location.Background.PermissionGrantedDescription
            )

            val NotGranted = PermissionItemUiModel.NotGranted(
                name = Strings.Location.Background.Name,
                description = Strings.Location.Background.Description,
                permissionNotGrantedDescription = Strings.Location.Background.PermissionNotGrantedDescription,
                buttonText = Strings.Location.Action
            )
        }
    }

    object Accessibility {

        val Granted = PermissionItemUiModel.Granted(
            name = Strings.Accessibility.Name,
            permissionGrantedDescription = Strings.Accessibility.PermissionGrantedDescription
        )

        val NotGranted = PermissionItemUiModel.NotGranted(
            name = Strings.Accessibility.Name,
            description = Strings.Accessibility.Description,
            permissionNotGrantedDescription = Strings.Accessibility.PermissionNotGrantedDescription,
            buttonText = Strings.Accessibility.Action
        )
    }
}
