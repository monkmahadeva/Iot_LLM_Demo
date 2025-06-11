package com.vfx.rightbrainstudios.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorDao {

    @Insert
    suspend fun insert(sensorEntity: SensorEntity)

    @Query("SELECT * FROM sensor_data ORDER BY id DESC LIMIT 10")
    fun getLastTen(): Flow<List<SensorEntity>>

    @Query("SELECT * FROM sensor_data WHERE deviceId = :deviceId ORDER BY id DESC LIMIT 10")
    fun getRecentSensorData(deviceId: String): Flow<List<SensorEntity>>

    @Query("DELETE FROM sensor_data")
    suspend fun clearAll()

    @Query("SELECT * FROM sensor_data")
    fun getAll():  Flow<List<SensorEntity>>
}