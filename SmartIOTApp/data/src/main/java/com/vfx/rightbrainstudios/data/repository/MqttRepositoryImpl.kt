package com.vfx.rightbrainstudios.data.repository

import com.vfx.rightbrainstudios.domain.repository.MqttRepository
import com.vfx.rightbrainstudios.data.remote.MqttManager
import javax.inject.Inject

class MqttRepositoryImpl @Inject constructor(
    private val mqttManager: MqttManager
) : MqttRepository {

    override fun connectAndSubscribe(topic: String, onMessage: (String) -> Unit) {
        mqttManager.connect(topic, onMessage)
    }

    override fun disconnect() {
        mqttManager.disconnect()
    }
}