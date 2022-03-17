package shuttle.permissions.model

import shuttle.design.AnyStringResource

internal sealed interface PermissionItemUiModel {

    val name: AnyStringResource

    fun isGranted() = this is Granted

    data class Granted(
        override val name: AnyStringResource,
        val permissionGrantedDescription: AnyStringResource
    ) : PermissionItemUiModel

    data class NotGranted(
        override val name: AnyStringResource,
        val description: AnyStringResource,
        val permissionNotGrantedDescription: AnyStringResource,
        val buttonText: AnyStringResource
    ) : PermissionItemUiModel
}
