package com.vfx.rightbrainstudios.data.repository

import com.vfx.rightbrainstudios.domain.model.SensorData
import com.vfx.rightbrainstudios.domain.repository.SensorRepository
import com.vfx.rightbrainstudios.data.model.SensorDao
import com.vfx.rightbrainstudios.data.mapper.toDomain
import com.vfx.rightbrainstudios.data.mapper.toEntity
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SensorRepositoryImpl @Inject constructor(
    private val dao: SensorDao
) : SensorRepository {

    override suspend fun getRecentSensorData(deviceId: String): List<SensorData> {
        return dao.getRecentSensorData(deviceId) // Flow<List<SensorEntity>>
            .first()                             // Collect to List<SensorEntity>
            .map { it.toDomain() }              // Convert each to SensorData
    }

    override suspend fun saveSensorData(deviceId: String, sensor: SensorData) {
        dao.insert(sensor.toEntity(deviceId))
    }
}