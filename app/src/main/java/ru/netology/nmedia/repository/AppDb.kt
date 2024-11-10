package ru.netology.nmedia.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nmedia.dao.GeoPointDao
import ru.netology.nmedia.entity.GeoPointEntity

@Database(entities = [GeoPointEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun geoPointDao(): GeoPointDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .build()
    }
}