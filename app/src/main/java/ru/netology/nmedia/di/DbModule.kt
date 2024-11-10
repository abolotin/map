package ru.netology.nmedia.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.dao.GeoPointDao
import ru.netology.nmedia.repository.AppDb
import ru.netology.nmedia.repository.GeoPointRepository
import ru.netology.nmedia.repository.GeoPointRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DbModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDb = Room
        .databaseBuilder(context, AppDb::class.java, "app.db")
        .build()

    @Provides
    fun providePostDao(db: AppDb) : GeoPointDao = db.geoPointDao()

    @Provides
    fun provideGeoPointRepository(geoPointDao: GeoPointDao): GeoPointRepository = GeoPointRepositoryImpl(geoPointDao)
}