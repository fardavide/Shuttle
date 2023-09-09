package shuttle.stats.data.mapper

import shuttle.apps.domain.model.AppId
import shuttle.apps.domain.model.AppModel
import shuttle.apps.domain.model.AppName
import shuttle.apps.domain.model.SuggestedAppModel
import shuttle.coordinates.domain.model.GeoHash
import shuttle.database.model.DatabaseAppId
import shuttle.database.model.DatabaseGeoHash


internal fun AppId.toDatabaseAppId() = DatabaseAppId(value)
internal fun GeoHash.toDatabaseGeoHash() = DatabaseGeoHash(value)

internal fun DatabaseAppId.toAppId() = AppId(value)
internal fun String.toAppName() = AppName(this)

internal fun toSuggestedAppModel(app: AppModel) = SuggestedAppModel(
    app.id,
    app.name,
    isSuggested = true
)
internal fun toNotSuggestedAppModel(app: AppModel) = SuggestedAppModel(
    app.id,
    app.name,
    isSuggested = false
)
