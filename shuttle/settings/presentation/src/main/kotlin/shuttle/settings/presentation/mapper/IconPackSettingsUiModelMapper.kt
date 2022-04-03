package shuttle.settings.presentation.mapper

import arrow.core.Option
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp
import shuttle.settings.presentation.model.IconPackSettingsItemUiModel
import studio.forface.shuttle.design.R

internal class IconPackSettingsUiModelMapper(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp
) {

    suspend fun toUiModels(
        iconPacks: Collection<AppModel>,
        selectedIconPack: Option<AppId>
    ): List<IconPackSettingsItemUiModel> {
        val systemDefaultUiModel = IconPackSettingsItemUiModel.SystemDefault(
            name = R.string.settings_icon_pack_system_default,
            isSelected = selectedIconPack.isEmpty()
        )
        val iconsPacksModels = iconPacks
            .map { toUiModel(it, isSelected = it.id == selectedIconPack.orNull()) }
        return listOf(systemDefaultUiModel) + iconsPacksModels
    }

    private suspend fun toUiModel(iconPack: AppModel, isSelected: Boolean) = IconPackSettingsItemUiModel.FromApp(
        id = iconPack.id,
        name = iconPack.name.value,
        icon = getSystemIconDrawableForApp(iconPack.id),
        isSelected = isSelected
    )
}
