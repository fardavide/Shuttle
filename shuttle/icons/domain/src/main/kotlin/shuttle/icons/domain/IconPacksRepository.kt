package shuttle.icons.domain

import shuttle.apps.domain.model.AppId
import shuttle.icons.domain.model.IconPack

interface IconPacksRepository {

    suspend fun loadIconPack(id: AppId): IconPack
}
