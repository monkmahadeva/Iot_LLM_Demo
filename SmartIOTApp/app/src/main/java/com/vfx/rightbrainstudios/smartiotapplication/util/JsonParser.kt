package com.vfx.rightbrainstudios.smartiotapplication.util


import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.vfx.rightbrainstudios.domain.model.SensorData

object JsonParser {
    private fun parseToMap(json: String): Map<String, Any> {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(
            Map::class.java,
            String::class.java,
            Any::class.java
        )
        val adapter = moshi.adapter<Map<String, Any>>(type)
        return adapter.fromJson(json) ?: throw IllegalArgumentException("Invalid JSON")
    }

    fun parseSensorData(json: String): SensorData { // TODO: Business logic to shift to domain and data#2
        val data = parseToMap(json)
        return SensorData(
            temperature = (data["temperature"] as? Double) ?: 0.0,
            humidity = ((data["humidity"] as? Double)?.toInt() ?: 0),
            timestamp = data["timestamp"] as? String ?: ""
        )
    }
}