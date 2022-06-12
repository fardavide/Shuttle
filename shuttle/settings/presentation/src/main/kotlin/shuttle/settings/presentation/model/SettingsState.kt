package shuttle.settings.presentation.model

import shuttle.design.util.Effect

data class SettingsState(
    val permissions: Permissions,
    val prioritizeLocation: PrioritizeLocation,
    val appVersion: String,
    val openOnboardingEffect: Effect<Unit>
) {

    sealed interface Permissions {

        object Loading : Permissions
        object Granted : Permissions
        object Denied : Permissions
    }

    sealed interface PrioritizeLocation {

        object Loading : PrioritizeLocation
        object False : PrioritizeLocation
        object True : PrioritizeLocation
    }

    companion object {

        val Loading = SettingsState(
            permissions = Permissions.Loading,
            prioritizeLocation = PrioritizeLocation.Loading,
            appVersion = "",
            openOnboardingEffect = Effect.empty()
        )
    }
}
