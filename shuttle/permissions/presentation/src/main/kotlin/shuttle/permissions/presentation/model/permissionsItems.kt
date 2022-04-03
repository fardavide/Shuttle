package shuttle.permissions.presentation.model

import studio.forface.shuttle.design.R

internal object PermissionItem {

    object Location {

        object Coarse {

            val Granted = PermissionItemUiModel.Granted(
                name = R.string.permissions_location_coarse_name,
                permissionGrantedDescription = R.string.permissions_location_coarse_description
            )

            val NotGranted = PermissionItemUiModel.NotGranted(
                name = R.string.permissions_location_coarse_name,
                description = R.string.permissions_location_coarse_description,
                permissionNotGrantedDescription = R.string.permissions_location_coarse_not_granted_description,
                buttonText = R.string.permissions_location_action
            )
        }

        object Fine {

            val Granted = PermissionItemUiModel.Granted(
                name = R.string.permissions_location_fine_name,
                permissionGrantedDescription = R.string.permissions_location_fine_granted_description
            )

            val NotGranted = PermissionItemUiModel.NotGranted(
                name = R.string.permissions_location_fine_name,
                description = R.string.permissions_location_fine_description,
                permissionNotGrantedDescription = R.string.permissions_location_fine_not_granted_description,
                buttonText = R.string.permissions_location_action
            )
        }

        object Background {

            val Granted = PermissionItemUiModel.Granted(
                name = R.string.permissions_location_background_name,
                permissionGrantedDescription = R.string.permissions_location_background_granted_description
            )

            val NotGranted = PermissionItemUiModel.NotGranted(
                name = R.string.permissions_location_background_name,
                description = R.string.permissions_location_background_description,
                permissionNotGrantedDescription = R.string.permissions_location_background_not_granted_description,
                buttonText = R.string.permissions_location_action
            )
        }
    }

    object Accessibility {

        val Granted = PermissionItemUiModel.Granted(
            name = R.string.permissions_accessibility_name,
            permissionGrantedDescription = R.string.permissions_accessibility_description
        )

        val NotGranted = PermissionItemUiModel.NotGranted(
            name = R.string.permissions_accessibility_name,
            description = R.string.permissions_accessibility_description,
            permissionNotGrantedDescription = R.string.permissions_accessibility_not_granted_description,
            buttonText = R.string.permissions_accessibility_action
        )
    }
}
