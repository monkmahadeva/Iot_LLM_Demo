package com.vfx.rightbrainstudios.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenAiData(
    val content: String = ""
)