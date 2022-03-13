package shuttle.permissions.model

sealed interface PermissionItemUiModel {

    val name: String

    fun isGranted() = this is Granted

    data class Granted(
        override val name: String,
        val permissionGrantedDescription: String
    ) : PermissionItemUiModel

    data class NotGranted(
        override val name: String,
        val description: String,
        val permissionNotGrantedDescription: String,
        val buttonText: String
    ) : PermissionItemUiModel
}
