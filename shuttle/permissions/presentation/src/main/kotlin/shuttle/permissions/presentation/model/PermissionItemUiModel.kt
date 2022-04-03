package shuttle.permissions.presentation.model

import androidx.annotation.StringRes

internal sealed interface PermissionItemUiModel {

    @get:StringRes val name: Int

    fun isGranted() = this is Granted

    data class Granted(
        @StringRes override val name: Int,
        @StringRes val permissionGrantedDescription: Int
    ) : PermissionItemUiModel

    data class NotGranted(
        @StringRes override val name: Int,
        @StringRes val description: Int,
        @StringRes val permissionNotGrantedDescription: Int,
        @StringRes val buttonText: Int
    ) : PermissionItemUiModel
}
