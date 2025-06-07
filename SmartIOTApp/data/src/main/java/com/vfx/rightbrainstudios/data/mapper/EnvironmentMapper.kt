package com.vfx.rightbrainstudios.data.mapper

import com.vfx.rightbrainstudios.domain.model.SensorData
import com.vfx.rightbrainstudios.data.model.SensorEntity

fun SensorEntity.toDomain(): SensorData {
    return SensorData(
        temperature = this.temperature,
        humidity = this.humidity,
        timestamp = this.timestamp
    )
}

// Optional — used only if saving back from domain to database
fun SensorData.toEntity(deviceId: String): SensorEntity {
    return SensorEntity(
        deviceId = deviceId,
        temperature = this.temperature,
        humidity = this.humidity,
        timestamp = this.timestamp
    )
}