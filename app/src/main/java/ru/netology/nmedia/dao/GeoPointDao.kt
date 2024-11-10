package ru.netology.nmedia.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.entity.GeoPointEntity

@Dao
interface GeoPointDao {
    @Query("SELECT * FROM GeoPointEntity ORDER BY title")
    fun getAll(): Flow<List<GeoPointEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(geoPoint: GeoPointEntity)

    @Query("DELETE FROM GeoPointEntity WHERE lat = :lat AND lng = :lng")
    suspend fun remove(lat: Double, lng: Double)
}