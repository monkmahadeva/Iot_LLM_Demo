package com.vfx.rightbrainstudios.data.mapper

import com.vfx.rightbrainstudios.domain.model.OpenAiData
import com.vfx.rightbrainstudios.data.remote.model.OpenAIResponse

fun OpenAIResponse.toDomain(): OpenAiData {
    val combinedContent = this.choices.joinToString(separator = "\n") { it.message.content }
    return OpenAiData(content = combinedContent)
}