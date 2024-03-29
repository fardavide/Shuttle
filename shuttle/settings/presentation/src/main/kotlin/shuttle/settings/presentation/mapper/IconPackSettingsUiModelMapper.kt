package shuttle.settings.presentation.mapper

import arrow.core.Either
import arrow.core.Option
import arrow.core.raise.either
import arrow.core.right
import org.koin.core.annotation.Factory
import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.GetAppError
import shuttle.icons.domain.usecase.GetSystemIconDrawableForApp
import shuttle.resources.R.string
import shuttle.settings.presentation.model.IconPackSettingsItemUiModel

@Factory
internal class IconPackSettingsUiModelMapper(
    private val getSystemIconDrawableForApp: GetSystemIconDrawableForApp
) {

    suspend fun toUiModels(
        iconPacks: Collection<AppModel>,
        selectedIconPack: Option<AppId>
    ): List<Either<GetAppError, IconPackSettingsItemUiModel>> {
        val systemDefaultUiModel = IconPackSettingsItemUiModel.SystemDefault(
            name = string.settings_icon_pack_system_default,
            isSelected = selectedIconPack.isNone()
        ).right()
        val iconsPacksModels = iconPacks
            .map { toUiModel(it, isSelected = it.id == selectedIconPack.getOrNull()) }
        return listOf(systemDefaultUiModel) + iconsPacksModels
    }

    private suspend fun toUiModel(
        iconPack: AppModel,
        isSelected: Boolean
    ): Either<GetAppError, IconPackSettingsItemUiModel.FromApp> = either {
        IconPackSettingsItemUiModel.FromApp(
            id = iconPack.id,
            name = iconPack.name.value,
            icon = getSystemIconDrawableForApp(iconPack.id).bind(),
            isSelected = isSelected
        )
    }
}
