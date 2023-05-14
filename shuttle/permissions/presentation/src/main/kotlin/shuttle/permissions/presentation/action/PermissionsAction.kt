package shuttle.permissions.presentation.action

internal sealed interface PermissionsAction {

    object RequestLocation : PermissionsAction
    object RequestBackgroundLocation : PermissionsAction
}
