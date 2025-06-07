package com.vfx.rightbrainstudios.data.usecaseimpl

import com.vfx.rightbrainstudios.data.di.IoDispatcher
import com.vfx.rightbrainstudios.domain.repository.MqttRepository
import com.vfx.rightbrainstudios.domain.usecase.SubscribeChartDataUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubscribeChartDataUseCaseImpl @Inject constructor(
    private val mqttRepository: MqttRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): SubscribeChartDataUseCase{
    override suspend fun invoke(topic: String, onMessage: (String) -> Unit) {
        withContext(ioDispatcher){
            mqttRepository.connectAndSubscribe(topic, onMessage)
        }
    }
}