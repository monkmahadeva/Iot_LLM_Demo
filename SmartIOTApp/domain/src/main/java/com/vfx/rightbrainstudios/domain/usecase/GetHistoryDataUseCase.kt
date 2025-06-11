package com.vfx.rightbrainstudios.domain.usecase

import com.vfx.rightbrainstudios.domain.model.SensorData

interface GetHistoryDataUseCase {
    suspend operator fun invoke(): List<SensorData>
}