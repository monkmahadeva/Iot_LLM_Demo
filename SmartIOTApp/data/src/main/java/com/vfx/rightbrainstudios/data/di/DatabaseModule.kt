package com.vfx.rightbrainstudios.data.di

import android.content.Context
import androidx.room.Room
import com.vfx.rightbrainstudios.data.model.SensorDao
import com.vfx.rightbrainstudios.data.model.SensorDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SensorDatabase =
        Room.databaseBuilder(context, SensorDatabase::class.java, "sensor_db").build()

    @Provides
    fun provideSensorDao(db: SensorDatabase): SensorDao = db.sensorDao()
}