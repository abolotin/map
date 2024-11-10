package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.GeoPoint

interface GeoPointRepository {
    val data: Flow<List<GeoPoint>>

    suspend fun save(geoPoint: GeoPoint)
    suspend fun remove(geoPoint: GeoPoint)
}