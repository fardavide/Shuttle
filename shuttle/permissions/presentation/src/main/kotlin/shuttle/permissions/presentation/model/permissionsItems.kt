package shuttle.permissions.presentation.model

import shuttle.resources.R.string

internal object PermissionItem {

    object Location {

        object Coarse {

            val Granted = PermissionItemUiModel.Granted(
                name = string.permissions_location_coarse_name,
                permissionGrantedDescription = string.permissions_location_coarse_description
            )

            val NotGranted = PermissionItemUiModel.NotGranted(
                name = string.permissions_location_coarse_name,
                description = string.permissions_location_coarse_description,
                permissionNotGrantedDescription = string.permissions_location_coarse_not_granted_description,
                buttonText = string.permissions_location_action
            )
        }

        object Fine {

            val Granted = PermissionItemUiModel.Granted(
                name = string.permissions_location_fine_name,
                permissionGrantedDescription = string.permissions_location_fine_granted_description
            )

            val NotGranted = PermissionItemUiModel.NotGranted(
                name = string.permissions_location_fine_name,
                description = string.permissions_location_fine_description,
                permissionNotGrantedDescription = string.permissions_location_fine_not_granted_description,
                buttonText = string.permissions_location_action
            )
        }

        object Background {

            val Granted = PermissionItemUiModel.Granted(
                name = string.permissions_location_background_name,
                permissionGrantedDescription = string.permissions_location_background_granted_description
            )

            val NotGranted = PermissionItemUiModel.NotGranted(
                name = string.permissions_location_background_name,
                description = string.permissions_location_background_description,
                permissionNotGrantedDescription = string.permissions_location_background_not_granted_description,
                buttonText = string.permissions_location_action
            )
        }
    }

    object Accessibility {

        val Granted = PermissionItemUiModel.Granted(
            name = string.permissions_accessibility_name,
            permissionGrantedDescription = string.permissions_accessibility_description
        )

        val NotGranted = PermissionItemUiModel.NotGranted(
            name = string.permissions_accessibility_name,
            description = string.permissions_accessibility_description,
            permissionNotGrantedDescription = string.permissions_accessibility_not_granted_description,
            buttonText = string.permissions_accessibility_action
        )
    }
}
