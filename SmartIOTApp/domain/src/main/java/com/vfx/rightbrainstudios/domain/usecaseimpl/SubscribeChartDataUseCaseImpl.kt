package com.vfx.rightbrainstudios.domain.usecaseimpl

import com.vfx.rightbrainstudios.domain.repository.MqttRepository
import com.vfx.rightbrainstudios.domain.usecase.SubscribeChartDataUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SubscribeChartDataUseCaseImpl(
    private val mqttRepository: MqttRepository,
    private val ioDispatcher: CoroutineDispatcher
): SubscribeChartDataUseCase{
    override suspend fun invoke(topic: String, onMessage: (String) -> Unit) {
        withContext(ioDispatcher){
            mqttRepository.connectAndSubscribe(topic, onMessage)
        }
    }
}