package shuttle.settings.presentation.action

import arrow.core.Option
import shuttle.apps.domain.model.AppId

sealed interface IconPacksSettingsAction {

    data class SetCurrentIconPack(val iconPackId: Option<AppId>) : IconPacksSettingsAction
}
