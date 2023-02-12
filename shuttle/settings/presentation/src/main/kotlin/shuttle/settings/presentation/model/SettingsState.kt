package shuttle.settings.presentation.model

import shuttle.design.util.Effect

data class SettingsState(
    val permissions: Permissions,
    val appVersion: String,
    val openOnboardingEffect: Effect<Unit>
) {

    sealed interface Permissions {

        object Loading : Permissions
        object Granted : Permissions
        object Denied : Permissions
    }

    companion object {

        val Loading = SettingsState(
            permissions = Permissions.Loading,
            appVersion = "",
            openOnboardingEffect = Effect.empty()
        )
    }
}
