package ru.netology.nmedia.entity

import androidx.room.Entity
import ru.netology.nmedia.dto.GeoPoint

@Entity(primaryKeys = ["lat", "lng"])
class GeoPointEntity(
    val lat: Double,
    val lng: Double,
    val title: String,
    val description: String
) {
    companion object {
        fun fromDto(geoPoint: GeoPoint) = GeoPointEntity(
            lat = geoPoint.lat,
            lng = geoPoint.lng,
            title = geoPoint.title,
            description = geoPoint.description
        )
    }

    fun toDto() = GeoPoint(
        lat = lat,
        lng = lng,
        title = title,
        description = description
    )
}

fun List<GeoPointEntity>.toDto() = map(GeoPointEntity::toDto)
