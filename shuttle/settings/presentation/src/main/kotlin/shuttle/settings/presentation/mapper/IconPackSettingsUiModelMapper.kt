package shuttle.settings.presentation.mapper

import arrow.core.Either
import arrow.core.Option
import arrow.core.continuations.either
import arrow.core.right
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.icons.domain.error.GetSystemIconError
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp
import shuttle.resources.R
import shuttle.settings.presentation.model.IconPackSettingsItemUiModel

@Factory
internal class IconPackSettingsUiModelMapper(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp
) {

    suspend fun toUiModels(
        iconPacks: Collection<AppModel>,
        selectedIconPack: Option<AppId>
    ): List<Either<GetSystemIconError, IconPackSettingsItemUiModel>> {
        val systemDefaultUiModel = IconPackSettingsItemUiModel.SystemDefault(
            name = R.string.settings_icon_pack_system_default,
            isSelected = selectedIconPack.isEmpty()
        ).right()
        val iconsPacksModels = iconPacks
            .map { toUiModel(it, isSelected = it.id == selectedIconPack.orNull()) }
        return listOf(systemDefaultUiModel) + iconsPacksModels
    }

    private suspend fun toUiModel(
        iconPack: AppModel,
        isSelected: Boolean
    ): Either<GetSystemIconError, IconPackSettingsItemUiModel.FromApp> = either {
        IconPackSettingsItemUiModel.FromApp(
            id = iconPack.id,
            name = iconPack.name.value,
            icon = getSystemIconDrawableForApp(iconPack.id).bind(),
            isSelected = isSelected
        )
    }
}
