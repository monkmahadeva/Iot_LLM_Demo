package com.vfx.rightbrainstudios.domain.usecase

import com.vfx.rightbrainstudios.domain.model.SensorData

interface GetRecentChartDataUseCase {
    suspend fun invoke(deviceId: String): List<SensorData>
}