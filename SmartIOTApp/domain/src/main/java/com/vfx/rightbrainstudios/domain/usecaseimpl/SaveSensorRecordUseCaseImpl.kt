package com.vfx.rightbrainstudios.domain.usecaseimpl

import com.vfx.rightbrainstudios.domain.model.SensorData
import com.vfx.rightbrainstudios.domain.repository.SensorRepository
import com.vfx.rightbrainstudios.domain.usecase.SaveSensorRecordUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SaveSensorRecordUseCaseImpl(
    private val sensorRepository: SensorRepository,
    private val dispatcher: CoroutineDispatcher
): SaveSensorRecordUseCase {
    override suspend fun invoke(deviceId: String, sensorData: SensorData){
        withContext(dispatcher) {
            sensorRepository.saveSensorData(deviceId,sensorData)
        }
    }

}