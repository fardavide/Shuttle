package shuttle.icons.domain

import shuttle.icons.domain.model.IconPack

interface IconPacksRepository {

    suspend fun loadIconPack(): IconPack
}
