package com.vfx.rightbrainstudios.domain.repository

import com.vfx.rightbrainstudios.domain.model.SensorData

interface SensorRepository {
    suspend fun saveSensorData(deviceId: String, sensorData: SensorData)
    suspend fun getRecentSensorData(deviceId: String): List<SensorData>
    suspend fun getAllSensorData(): List<SensorData>
}