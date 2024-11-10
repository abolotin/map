package ru.netology.nmedia.dto

import ru.netology.nmedia.entity.GeoPointEntity

data class GeoPoint(
    val lat: Double,
    val lng: Double,
    val title: String = "",
    val description: String = ""
) {
    fun toEntity() = GeoPointEntity(
        lat = lat,
        lng = lng,
        title = title,
        description = description
    )
}
