package com.vfx.rightbrainstudios.data.remote.model
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenAIResponse(
    val choices: List<Choice>,
    val usage: Usage? = null
)

@JsonClass(generateAdapter = true)
data class Choice(
    val index: Int,
    val message: Message,

    @Json(name = "finish_reason")
    val finishReason: String
)
//
//@JsonClass(generateAdapter = true)
//data class Message(
//    val role: String,
//    val content: String
//)

@JsonClass(generateAdapter = true)
data class Usage(
    @Json(name = "prompt_tokens")
    val promptTokens: Int,

    @Json(name = "completion_tokens")
    val completionTokens: Int,

    @Json(name = "total_tokens")
    val totalTokens: Int
)