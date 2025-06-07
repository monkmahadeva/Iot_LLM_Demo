package com.vfx.rightbrainstudios.domain.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class SensorData (
    val temperature: Double,
    val humidity: Int,
    val timestamp: String // or Long depending on use
)
