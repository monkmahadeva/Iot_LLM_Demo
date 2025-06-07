package com.vfx.rightbrainstudios.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "sensor_data")
@JsonClass(generateAdapter = true)
data class SensorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val deviceId: String,
    val temperature: Double,
    val humidity: Int,
    val timestamp: String // or use `Long` if you prefer epoch
)