package com.vfx.rightbrainstudios.domain.usecaseimpl


import com.vfx.rightbrainstudios.domain.model.SensorData
import com.vfx.rightbrainstudios.domain.repository.SensorRepository
import com.vfx.rightbrainstudios.domain.usecase.GetRecentChartDataUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetRecentChartDataUseCaseImpl(
    private val sensorRepository: SensorRepository,
    private var dispatcher: CoroutineDispatcher
): GetRecentChartDataUseCase {
    override suspend fun invoke(deviceId: String): List<SensorData> {
       return withContext(dispatcher){
            sensorRepository.getRecentSensorData(deviceId)
        }
    }

}