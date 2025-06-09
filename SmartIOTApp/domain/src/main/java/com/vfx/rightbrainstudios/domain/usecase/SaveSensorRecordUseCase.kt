package com.vfx.rightbrainstudios.domain.usecase

import com.vfx.rightbrainstudios.domain.model.SensorData

interface SaveSensorRecordUseCase {
    suspend fun invoke(deviceId : String, sensorData: SensorData)
}