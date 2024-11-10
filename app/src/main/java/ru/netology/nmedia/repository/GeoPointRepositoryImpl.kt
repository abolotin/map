package ru.netology.nmedia.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.nmedia.dao.GeoPointDao
import ru.netology.nmedia.dto.GeoPoint
import ru.netology.nmedia.entity.GeoPointEntity
import ru.netology.nmedia.entity.toDto
import javax.inject.Inject

class GeoPointRepositoryImpl @Inject constructor(
    private val geoPointDao: GeoPointDao,
) : GeoPointRepository {
    override val data = geoPointDao.getAll()
        .map(List<GeoPointEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override suspend fun save(geoPoint: GeoPoint) {
        geoPointDao.insert(geoPoint.toEntity())
    }

    override suspend fun remove(geoPoint: GeoPoint) {
        geoPointDao.remove(geoPoint.lat, geoPoint.lng)
    }
}
