package com.vfx.rightbrainstudios.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenAIRequest(
    @Json(name = "model") val model: String,
    @Json(name = "messages") val messages: List<Message>,
    @Json(name = "max_tokens") val maxTokens: Int = 150,
    @Json(name = "temperature") val temperature: Double = 0.7
)