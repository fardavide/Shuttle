package shuttle.coordinates.data.mapper

import android.location.Location
import shuttle.coordinates.domain.model.GeoHash

class GeoHashMapper {

    fun toGeoHash(location: Location): GeoHash = toGeoHash(location.latitude, location.longitude)

    fun toGeoHash(latitude: Double, longitude: Double): GeoHash {
        val geoHashString = ch.hsr.geohash.GeoHash
            .withCharacterPrecision(latitude, longitude, 7)
            .toBase32()
        return GeoHash(geoHashString)
    }
}
