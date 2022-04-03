package shuttle.permissions.presentation.model

internal data class PermissionItemsUiModel(
    val coarseLocation: PermissionItemUiModel,
    val fineLocation: PermissionItemUiModel,
    val backgroundLocation: PermissionItemUiModel,
    val accessibilityService: PermissionItemUiModel,
) {

    fun areAllGranted() =
        coarseLocation.isGranted() &&
            fineLocation.isGranted() &&
            backgroundLocation.isGranted() &&
            accessibilityService.isGranted()
}
