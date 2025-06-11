package com.vfx.rightbrainstudios.domain.usecaseimpl

import com.vfx.rightbrainstudios.domain.model.SensorData
import com.vfx.rightbrainstudios.domain.repository.SensorRepository
import com.vfx.rightbrainstudios.domain.usecase.GetHistoryDataUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetHistoryDataUseCaseImpl(
    private val sensorRepository: SensorRepository,
    private val ioDispatcher: CoroutineDispatcher
): GetHistoryDataUseCase {
    override suspend fun invoke(): List<SensorData> {
        return withContext(ioDispatcher){
            sensorRepository.getAllSensorData()
        }
    }
}