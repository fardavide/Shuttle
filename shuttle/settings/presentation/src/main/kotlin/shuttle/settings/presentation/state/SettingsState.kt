package shuttle.settings.presentation.state

import shuttle.design.util.Effect

data class SettingsState(
    val permissions: Permissions,
    val appVersion: String,
    val isDataCollectionEnabled: Boolean,
    val openOnboardingEffect: Effect<Unit>,
    val shouldShowConsents: Boolean,
    val shouldShowStatisticsItem: Boolean,
    val useExperimentalAppSorting: Boolean
) {

    sealed interface Permissions {

        data object Loading : Permissions
        data object Granted : Permissions
        data object Denied : Permissions
    }

    companion object {

        val Loading = SettingsState(
            permissions = Permissions.Loading,
            appVersion = "",
            isDataCollectionEnabled = false,
            openOnboardingEffect = Effect.empty(),
            shouldShowConsents = false,
            shouldShowStatisticsItem = false,
            useExperimentalAppSorting = false
        )
    }
}
