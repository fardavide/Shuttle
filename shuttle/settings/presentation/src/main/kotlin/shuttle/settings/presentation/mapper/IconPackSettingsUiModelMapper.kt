package shuttle.settings.presentation.mapper

import arrow.core.Option
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp
import shuttle.settings.presentation.model.IconPackSettingsUiModel
import shuttle.settings.presentation.resources.Strings

internal class IconPackSettingsUiModelMapper(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp
) {

    suspend fun toUiModels(
        iconPacks: Collection<AppModel>,
        selectedIconPack: Option<AppId>
    ): List<IconPackSettingsUiModel> {
        val systemDefaultUiModel = IconPackSettingsUiModel.SystemDefault(
            name = Strings.IconPack::SystemDefault,
            isSelected = selectedIconPack.isEmpty()
        )
        val iconsPacksModels = iconPacks
            .map { toUiModel(it, isSelected = it.id == selectedIconPack.orNull()) }
        return listOf(systemDefaultUiModel) + iconsPacksModels
    }

    private suspend fun toUiModel(iconPack: AppModel, isSelected: Boolean) = IconPackSettingsUiModel.FromApp(
        name = iconPack.name.value,
        icon = getSystemIconDrawableForApp(iconPack.id),
        isSelected = isSelected
    )
}
