package com.vfx.rightbrainstudios.domain.repository

interface MqttRepository {
    fun connectAndSubscribe(topic: String, onMessage: (String) -> Unit)
    fun disconnect() //TODO:
}